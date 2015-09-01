package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;

public class Named extends UseRestriction {

    @JsonProperty
    private String type = "named";

    @JsonProperty
    private String name;

    public Named() {
    }

    public Named(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Named &&
                Objects.equal(this.name, ((Named) o).name);
    }

    @Override
    public OntClass createOntologicalRestriction(OntModel model) {
        return model.createClass(name);
    }

    public boolean visitAndContinue(UseRestrictionVisitor visitor) {
        return true;
    }
}