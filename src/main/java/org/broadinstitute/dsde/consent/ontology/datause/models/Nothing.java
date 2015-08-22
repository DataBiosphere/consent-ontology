package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;

public class Nothing extends UseRestriction {

    public Nothing() {}

    public String toString() {
        return String.format("{ \"type\": \"nothing\" }");
    }

    public int hashCode() { return "nothing".hashCode(); }

    public boolean equals(Object o) {
        if(!(o instanceof Nothing)) { return false; }
        return true;
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.getOntClass(OWL.Nothing.getURI());
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return true;
    }
}