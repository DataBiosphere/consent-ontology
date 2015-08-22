package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class Some extends UseRestriction {
    private String property;
    private UseRestriction object;

    public Some() {}

    public Some(String prop, UseRestriction obj) {
        this.property = prop;
        this.object = obj;
    }

    public String getProperty() { return property; }
    public UseRestriction getTarget() { return object; }

    public void setProperty(String p) { property = p; }
    public void setTarget(UseRestriction r) { object = r; }

    public String toString() {
        return String.format("{ \"type\": \"some\", \"property\": \"%s\", \"target\": %s }",
                property, object.toString());
    }

    public int hashCode() {
        return 37 * property.hashCode() + object.hashCode();
    }

    public boolean equals(Object o) {
        if(!(o instanceof Some)) return false;
        Some r = (Some)o;
        return property.equals(r.property) && object.equals(r.object);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        Property prop = model.createProperty(property);
        Resource objectClass = object.createOntologicalRestriction(model);
        return model.createSomeValuesFromRestriction(null, prop, objectClass);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return object.visit(visitor);
    }

}
