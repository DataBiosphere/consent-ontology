package org.broadinstitute.dsde.consent.ontology.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;
import org.elasticsearch.client.RestClient;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Integration test classes use the maven integration framework to start up an elastic search instance.
 * Tests can be run locally if a local docker es instance is manually started up:
 * docker run -p 9200:9200 -e "xpack.security.enabled=false" -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" docker.elastic.co/elasticsearch/elasticsearch:5.5.0
 */
@Category(IntegrationTest.class)
public class ElasticSearchAutocompleteAPITest {

    private static ElasticSearchAutocompleteAPI autocompleteAPI;
    private static final String INDEX_NAME = "ontology-integration";
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPITest.class);
    private static Gson gson = new GsonBuilder().create();

    @BeforeClass
    public static void setUp() throws Exception {
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX_NAME);
        configuration.setServers(Collections.singletonList("localhost"));
        autocompleteAPI = new ElasticSearchAutocompleteAPI(configuration);
        RestClient client = org.broadinstitute.dsde.consent.ontology.service.ElasticSearchSupport.createRestClient(configuration);
        IndexSupport.createIndex(client, INDEX_NAME);
        IndexSupport.populateIndex(client, INDEX_NAME);
    }

    @After
    public void shutDown() throws Exception {
    }

    @Test
    public void testOrchestrationReIndex() throws Exception {
        String[] terms = new String[] {
            "http://purl.obolibrary.org/obo/DOID_0050524",
            "http://purl.obolibrary.org/obo/DOID_0050700",
            "http://purl.obolibrary.org/obo/DOID_0050711",
            "http://purl.obolibrary.org/obo/DOID_0050813",
            "http://purl.obolibrary.org/obo/DOID_0050825",
            "http://purl.obolibrary.org/obo/DOID_0050827",
            "http://purl.obolibrary.org/obo/DOID_0060334",
            "http://purl.obolibrary.org/obo/DOID_10516",
            "http://purl.obolibrary.org/obo/DOID_10632",
            "http://purl.obolibrary.org/obo/DOID_114",
            "http://purl.obolibrary.org/obo/DOID_11516",
            "http://purl.obolibrary.org/obo/DOID_11569",
            "http://purl.obolibrary.org/obo/DOID_11712",
            "http://purl.obolibrary.org/obo/DOID_11714",
            "http://purl.obolibrary.org/obo/DOID_11717",
            "http://purl.obolibrary.org/obo/DOID_11817",
            "http://purl.obolibrary.org/obo/DOID_12325",
            "http://purl.obolibrary.org/obo/DOID_12326",
            "http://purl.obolibrary.org/obo/DOID_12387",
            "http://purl.obolibrary.org/obo/DOID_12388",
            "http://purl.obolibrary.org/obo/DOID_12785",
            "http://purl.obolibrary.org/obo/DOID_13499",
            "http://purl.obolibrary.org/obo/DOID_14110",
            "http://purl.obolibrary.org/obo/DOID_14557",
            "http://purl.obolibrary.org/obo/DOID_162",
            "http://purl.obolibrary.org/obo/DOID_1659",
            "http://purl.obolibrary.org/obo/DOID_1682",
            "http://purl.obolibrary.org/obo/DOID_1725",
            "http://purl.obolibrary.org/obo/DOID_175",
            "http://purl.obolibrary.org/obo/DOID_1837",
            "http://purl.obolibrary.org/obo/DOID_2174",
            "http://purl.obolibrary.org/obo/DOID_2352",
            "http://purl.obolibrary.org/obo/DOID_2596",
            "http://purl.obolibrary.org/obo/DOID_3303",
            "http://purl.obolibrary.org/obo/DOID_3393",
            "http://purl.obolibrary.org/obo/DOID_363",
            "http://purl.obolibrary.org/obo/DOID_4079",
            "http://purl.obolibrary.org/obo/DOID_4205",
            "http://purl.obolibrary.org/obo/DOID_5546",
            "http://purl.obolibrary.org/obo/DOID_5612",
            "http://purl.obolibrary.org/obo/DOID_5877",
            "http://purl.obolibrary.org/obo/DOID_6000",
            "http://purl.obolibrary.org/obo/DOID_602",
            "http://purl.obolibrary.org/obo/DOID_688",
            "http://purl.obolibrary.org/obo/DOID_734",
            "http://purl.obolibrary.org/obo/DOID_8514",
            "http://purl.obolibrary.org/obo/DOID_8515",
            "http://purl.obolibrary.org/obo/DOID_8556",
            "http://purl.obolibrary.org/obo/DOID_8635",
            "http://purl.obolibrary.org/obo/DOID_8947",
            "http://purl.obolibrary.org/obo/DOID_9351",
            "http://purl.obolibrary.org/obo/DOID_9352",
            "http://purl.obolibrary.org/obo/DOID_9409",
            "http://purl.obolibrary.org/obo/DOID_9432",
            "http://purl.obolibrary.org/obo/DOID_9744",
            "http://purl.obolibrary.org/obo/DOID_9955"
        };
        List<String> errors = new ArrayList<>();
        Set<TermResource> termResources = Arrays.stream(terms).
            map(t -> {
                List<TermResource> termList = new ArrayList<>();
                try { termList.addAll(autocompleteAPI.lookupById(t)); }
                catch (IOException e) { errors.add(e.getMessage()); }
                return termList;
            }).
            flatMap(List::stream).
            distinct().
            collect(Collectors.toSet());
        logTerms("testOrchestrationReIndex", termResources);
        Assert.assertTrue(!termResources.isEmpty());
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testLookup() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        logTerms("testLookup", termResource);
        Assert.assertTrue(termResource.size() == 1);
        List<String> synonyms = splitSynonymList(termResource.get(0).getSynonyms());
        Assert.assertTrue(synonyms.contains("cancer"));
    }

    @Test
    public void testLookupWithTags() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("Disease"), "cancer", 1);
        logTerms("testLookupWithTags", termResource);
        Assert.assertTrue(termResource.size() == 1);
        List<String> synonyms = splitSynonymList(termResource.get(0).getSynonyms());
        Assert.assertTrue(synonyms.contains("cancer"));
    }

    @Test
    public void testLookupWithWrongTags() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("Organization"), "cancer", 1);
        logTerms("testLookupWithWrongTags", termResource);
        Assert.assertTrue(termResource.size() == 0);
    }

    @Test
    public void testLookupWithCount() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 7);
        logTerms("testLookupWithCount", termResource);
        Assert.assertTrue(termResource.size() == 7);
    }

    @Test
    public void  testLookupById() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookupById("http://purl.obolibrary.org/obo/DOID_162");
        logTerms("testLookupById", termResource);
        Assert.assertNotNull(termResource);
    }

    private static List<String> splitSynonymList(List<String> synonyms) {
        return synonyms.
            stream().
            map(s -> s.split(" ")).
            flatMap(Arrays::stream).
            distinct().
            collect(Collectors.toList());
    }

    private static void logTerms(String testName, Collection<TermResource> termResources) {
        termResources.forEach( t -> logger.debug(testName + ": " + gson.toJson(t)));
    }

}
