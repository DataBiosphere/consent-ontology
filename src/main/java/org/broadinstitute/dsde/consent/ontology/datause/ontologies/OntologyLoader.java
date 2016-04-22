package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.JenaException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.OntologyModule;
import org.mindswap.pellet.jena.PelletReasonerFactory;

public class OntologyLoader {

    private static final Logger LOG = Logger.getLogger(OntologyModule.class);
    
    public static void main(String[] args) throws IOException, OWLOntologyCreationException {

    }

    /*
    private static String FIELD_ID = "id";
    private static String FIELD_LABEL = "label";
    private static String FIELD_DEFINITION = "definition";
    private static String FIELD_SYNONYM = "synonym";
    private static String FIELD_DEFINITION_CLASS = "IAO_0000115";
     */
    public static void loadOntology(InputStream reader, OntModel model)
            throws OWLOntologyCreationException, IOException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        assert manager != null : "Manager shouldn't be null";

        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(reader);
        assert ontology != null : "Ontology shouldn't be null";

        HashMap<String, OWLAnnotationProperty> annotationProperties = new HashMap<>();
        Stream<OWLAnnotationProperty> properties = ontology.annotationPropertiesInSignature();
        properties.forEach(property -> {
            annotationProperties.put(property.getIRI().getRemainder().get(), property);
        });

        OWLAnnotationProperty label = annotationProperties.get("label");
        assert label != null : "Need label annotation property";

        OWLAnnotationProperty deprecated = annotationProperties.get("deprecated");

        ontology.classesInSignature().forEach(owlClass -> {
            /*
            OWLAnnotationValueVisitorEx<String> visitor = new OWLAnnotationValueVisitorEx<String>() {
                @Override
                public String visit(IRI iri) {
                    return iri.toString();
                }

                @Override
                public String visit(OWLAnonymousIndividual owlAnonymousIndividual) {
                    return owlAnonymousIndividual.toStringID();
                }

                @Override
                public String visit(OWLLiteral owlLiteral) {
                    return owlLiteral.getLiteral();
                }
            };
             */

            // Do not load deprecated classes.
            if (!(deprecated != null && EntitySearcher.getAnnotations(owlClass, ontology, deprecated).count() > 0)) {
                String id = owlClass.toStringID();
                OntClass ontClass = model.createClass(id);

                /*
                String labelString = null;
                Set<OWLAnnotation> labels = owlClass.getAnnotations(ontology, label);
                if(labels.size() >= 1) {
                    labelString = labels.iterator().next().getValue().accept(visitor);
                    LOG.info(String.format("%s (%s)", labelString, id));
                }
                 */
                Stream<OWLClassExpression> superClasses = EntitySearcher.getSuperClasses(owlClass, ontology);
                superClasses.forEach(expr -> {
                    if (expr instanceof OWLClass) {
                        OWLClass cexpr = (OWLClass) expr;
                        // this ignores some obvious restriction / intersection classes.
                        OntClass superClass = model.createClass(cexpr.toStringID());
                        ontClass.addSuperClass(superClass);
                    }
                });
            }

        });
    }

    public static void load(InputStream reader, OntModel model) {
        OntModel ontoModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC, null);
        try {
            try {
                ontoModel.read(reader, null);
            } catch (Exception e) {
                LOG.error("ERROR1", e);
            }
        } catch (JenaException je) {
            LOG.error("ERROR2", je);
        }
        model = ontoModel;
    }
}
