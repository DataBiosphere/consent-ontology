package org.broadinstitute.dsde.consent.ontology.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermParent {
    public String id;
    public Integer order;

    public TermParent() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
