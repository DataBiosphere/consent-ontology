package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

public class Named extends UseRestriction {
    private String name;

    public Named() {}
    public Named(String name) { this.name = name; }

    public String toString() {
        return String.format("{ \"type\": \"named\", \"name\": \"%s\" }", name);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int hashCode() { return name.hashCode(); }

    public boolean equals(Object o) {
        if(!(o instanceof Named)) { return false; }
        Named n = (Named)o;
        return name.equals(n.name);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.createClass(name);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return true;
    }
}