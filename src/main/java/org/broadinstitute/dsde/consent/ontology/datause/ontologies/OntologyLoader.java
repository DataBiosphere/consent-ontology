package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class OntologyLoader {

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
        for (OWLAnnotationProperty property: ontology.getAnnotationPropertiesInSignature()) {
            annotationProperties.put(property.getIRI().getFragment(), property);
        }

        OWLAnnotationProperty label = annotationProperties.get("label");
        assert label != null : "Need label annotation property";

        OWLAnnotationProperty deprecated = annotationProperties.get("deprecated");

        for (OWLClass owlClass : ontology.getClassesInSignature()) {
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
            if (deprecated != null && owlClass.getAnnotations(ontology, deprecated).size() != 0) {
                continue;
            }

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

            Set<OWLClassExpression> superClasses = owlClass.getSuperClasses(ontology);
            for(OWLClassExpression expr : superClasses) {

                if(expr instanceof OWLClass) {

                    OWLClass cexpr = (OWLClass)expr;
                    // this ignores some obvious restriction / intersection classes.
                    OntClass superClass = model.createClass(cexpr.toStringID());
                    ontClass.addSuperClass(superClass);
                }
            }
        }
    }
}
