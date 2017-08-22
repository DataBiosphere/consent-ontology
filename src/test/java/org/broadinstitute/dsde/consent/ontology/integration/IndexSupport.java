package org.broadinstitute.dsde.consent.ontology.integration;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchSupport;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Note that most of this class is copied from Consent which is the artifact that
 * handles bulk uploads to Elastic Search. For integration testing, we need to
 * do the same thing. Ontology has upgraded to OWL API version 5, while Consent is
 * still using version 3. The v5 code is a lot cleaner than v3, so this can serve as
 * a model for upgrading Consent.
 */
public class IndexSupport {

    private static final Logger logger = LoggerFactory.getLogger(IndexSupport.class);
    static final String FIELD_DEFINITION_PROPERTY = "IAO_0000115";
    static final String FIELD_HAS_EXACT_SYNONYM_PROPERTY = "hasExactSynonym";
    static final String FIELD_LABEL_PROPERTY = "label";
    static final String FIELD_DEPRECATED_PROPERTY = "deprecated";

    public static void createIndex(Client client, ElasticSearchConfiguration config) throws Exception {
        // Check for the index first:
        Response getResponse = client.
            target(ElasticSearchSupport.getIndexPath(config)).
            request(MediaType.APPLICATION_JSON).
            get();
        logger.debug(getResponse.toString());
        if (getResponse.getStatus() != 200) {
            Response createResponse = client.
                target(ElasticSearchSupport.getIndexPath(config)).
                request(MediaType.APPLICATION_JSON).
                put(Entity.json(""));
            logger.debug(createResponse.toString());
            if (createResponse.getStatus() != 200) {
                throw new Exception("Unable to create index: " + createResponse.toString());
            }
        }
    }

    public static void populateIndex(ElasticSearchConfiguration config) throws Exception {
        uploadFile(config, Resources.getResource("data-use.owl"), "Organization");
        uploadFile(config, Resources.getResource("diseases.owl"), "Disease");
    }


    /*
     * The following are utility methods to facilitate iterating over an owl file and pushing it to ES.
     */


    private static void uploadFile(ElasticSearchConfiguration config, URL fileUrl, String ontologyType) throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new FileInputStream(new File(fileUrl.toURI())));
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
        Set<OWLClass> owlClasses = ontology.classesInSignature().collect(Collectors.toSet());
        Collection<Term> terms = owlClasses.
            stream().
            map(o -> generateTerm(o, ontologyType, ontology, reasoner)).
            collect(Collectors.toList());
        Boolean uploaded = bulkUploadTerms(config, terms);
        logger.info("Successful upload?: " + uploaded);
    }

    private static Term generateTerm(OWLClass owlClass, String ontologyType, OWLOntology ontology, OWLReasoner reasoner) {
        Term term = new Term(owlClass.toStringID(), ontologyType);
        Set<OWLAnnotation> classAnnotations = EntitySearcher.getAnnotations(owlClass, ontology).collect(Collectors.toSet());

        for (OWLAnnotation annotation : classAnnotations) {
            String propertyName = annotation.getProperty().getIRI().getRemainder().orElse("");
            String propertyValue = "";
            if (annotation.getValue().asLiteral().isPresent()) {
                propertyValue = annotation.getValue().asLiteral().get().getLiteral();
            }
            if (propertyName.equals(FIELD_DEPRECATED_PROPERTY)) {
                term.setUsable(false);
            }
            if (propertyName.equals(FIELD_HAS_EXACT_SYNONYM_PROPERTY)) {
                term.addSynonym(propertyValue);
            }
            if (propertyName.equals(FIELD_LABEL_PROPERTY)) {
                term.addLabel(propertyValue);
            }
            if (propertyName.equals(FIELD_DEFINITION_PROPERTY)) {
                term.addDefinition(propertyValue);
            }
        }

        int position = 0;
        for (Set<OWLClass> parentSet : getFilteredParentSets(owlClass, reasoner)) {
            position ++;
            for (OWLClass p : parentSet) {
                term.addParent(p.toStringID(), position);
            }
        }
        logger.debug("Generated term: " + term.toString());
        return term;

    }

    private static List<Set<OWLClass>> getFilteredParentSets(OWLClass owlClass, OWLReasoner reasoner) {
        List<Set<OWLClass>> parentSets = getParentSets(owlClass, reasoner);
        Collections.reverse(parentSets);
        List<Set<OWLClass>> filteredSets = new ArrayList<>();
        List<String> owlClassCache = new ArrayList<>();
        for (Set<OWLClass> classSet : parentSets) {
            // For any nodes in this set that have not been seen, create a new node set for adding
            Set<OWLClass> filteredParentSet = classSet.stream().
                filter(oc -> !owlClassCache.contains(oc.toStringID())).
                collect(Collectors.toSet());
            // Make sure all new nodes have their IDs added to the cache for future cache checking
            owlClassCache.addAll(filteredParentSet.stream().map(OWLClass::toStringID).collect(Collectors.toList()));
            // Finally, if we have a non-empty node, make sure it gets back into the queue
            if (!filteredParentSet.isEmpty()) {
                filteredSets.add(filteredParentSet);
            }
        }
        Collections.reverse(filteredSets);
        return filteredSets;
    }

    private static List<Set<OWLClass>> getParentSets(OWLClass owlClass, OWLReasoner reasoner) {
        List<Set<OWLClass>> parents = new ArrayList<>();
        Set<OWLClass> parentSet = reasoner.
            getSuperClasses(owlClass, true).
            entities().
            collect(Collectors.toSet());
        Set<OWLClass> validParentSet = new HashSet<>();
        parentSet.forEach(p -> {
            if (isValidOWLClass(p)) {
                validParentSet.add(p);
            }
        });
        if (!validParentSet.isEmpty()) {
            parents.add(validParentSet);
        }
        return validParentSet.isEmpty()
            ? parents
            : Stream.concat(
            parents.stream(),
            validParentSet.stream().map(p -> getParentSets(p, reasoner)).flatMap(List::stream)
        ).collect(Collectors.toList());
    }

    private static Boolean isValidOWLClass(OWLClass owlClass) {
        return owlClass != null &&
            owlClass.isOWLClass() &&
            !owlClass.isOWLThing() &&
            !owlClass.isOWLNothing();
    }

    /**
     * Had trouble with the jersey client and bulk uploads. The ES RestClient does a good job of this.
     */
    private static Boolean bulkUploadTerms(ElasticSearchConfiguration config, Collection<Term> terms) throws Exception {
        HttpHost[] hosts = config.
            getServers().
            stream().
            map(server -> new HttpHost(server, 9200, "http")).
            collect(Collectors.toList()).toArray(new HttpHost[config.getServers().size()]);
        RestClient client = RestClient.builder(hosts).build();

        // Set the partition relatively small so we can fail fast for incremental uploads
        List<List<Term>> termLists = Lists.partition(new ArrayList<>(terms), 100);
        for (List<Term> termList: termLists) {
            final CountDownLatch latch = new CountDownLatch(termList.size());
            ResponseListener listener = createResponseListener(latch);
            for (Term term: termList) {
                logger.debug("Posting term: " + term.toString());
                HttpEntity entity = new NStringEntity(
                    term.toString(),
                    ContentType.APPLICATION_JSON);
                client.performRequestAsync("PUT",
                    getTermIdPath(config.getIndex(), term.getId()),
                    Collections.emptyMap(),
                    entity,
                    listener,
                    IndexSupport.jsonHeader);
            }
            latch.await();
        }
        client.close();
        return true;
    }

    private static ResponseListener createResponseListener(CountDownLatch latch) {
        return new ResponseListener() {
            @Override
            public void onSuccess(org.elasticsearch.client.Response response) {
                logger.debug("Response Listener Success: " + response.toString());
                latch.countDown();
            }
            @Override
            public void onFailure(Exception exception) {
                latch.countDown();
            }
        };
    }

    private static String getTermIdPath(String index, String termId) throws UnsupportedEncodingException {
        return "/" + index + "/ontology_term/" + URLEncoder.encode(termId, "UTF-8");
    }

    private static Header jsonHeader = new BasicHeader("Content-Type", "application/json");

}
