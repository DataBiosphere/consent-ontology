package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TermResource {

    public String id;

    public String ontology;

    public Boolean usable;

    public String label;

    public String definition;

    public List<String> synonyms;

    public List<TermParent> parents;

    public TermResource() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public void setUsable(Boolean usable) {
        this.usable = usable;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setParents(List<TermParent> parents) {
        this.parents = parents;
    }

}
