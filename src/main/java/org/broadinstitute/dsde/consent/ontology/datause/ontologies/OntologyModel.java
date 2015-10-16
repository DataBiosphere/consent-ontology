package org.broadinstitute.dsde.consent.ontology.datause.ontologies;

import com.google.inject.ImplementedBy;
import com.hp.hpl.jena.ontology.OntModel;
import java.io.IOException;
import java.util.Collection;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

@ImplementedBy(OntologyList.class)
public interface OntologyModel {
    
    Collection<String> getResources();
        
    OntModel loadOntModel() throws IOException, OWLOntologyCreationException;

    OntModel loadBaseOntModel() throws IOException, OWLOntologyCreationException;

    OntModel getModel() throws IOException, OWLOntologyCreationException;
}
