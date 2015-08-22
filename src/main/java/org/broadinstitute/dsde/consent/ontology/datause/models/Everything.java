package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.vocabulary.OWL;

public class Everything extends UseRestriction {

    public Everything() {}

    public String toString() {
        return String.format("{ \"type\": \"everything\" }");
    }

    public int hashCode() { return "everything".hashCode(); }

    public boolean equals(Object o) {
        if(!(o instanceof Everything)) { return false; }
        return true;
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.getOntClass(OWL.Thing.getURI());
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return true;
    }
}