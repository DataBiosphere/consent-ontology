package org.broadinstitute.dsde.consent.ontology.datause.models;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class ResearchPurpose {

    private String id, researcher;
    private UseRestriction purpose;
    private String[] sensitiveTags;

    public ResearchPurpose() {}
    public ResearchPurpose(String id, String researcher, UseRestriction purpose, String[] sensitiveTags) {
        this.id = id;
        this.researcher = researcher;
        this.purpose = purpose;
        this.sensitiveTags = sensitiveTags.clone();
    }

    public String getId() { return id; }
    public String getResearcher() { return researcher; }
    public String[] getSensitiveTags() { return sensitiveTags; }
    public UseRestriction getPurpose() { return purpose; }

    public void setId(String id) { this.id = id; }
    public void setResearcher(String researcher) { this.researcher = researcher; }
    public void setSensitiveTags(String[] tags) { this.sensitiveTags = tags.clone(); }
    public void setPurpose(UseRestriction purpose) { this.purpose = purpose; }

    public int hashCode() { return id.hashCode(); }
    public boolean equals(Object o) {
        return ((o instanceof ResearchPurpose) && ((ResearchPurpose)o).id.equals(id));
    }

    public String toString() {
        try {
            return (new ObjectMapper()).writer().writeValueAsString(this);
        } catch (IOException e) {
            return id;
        }
    }
}
