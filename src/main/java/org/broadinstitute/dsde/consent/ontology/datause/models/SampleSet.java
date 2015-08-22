package org.broadinstitute.dsde.consent.ontology.datause.models;

public class SampleSet {

    private String id, owner;
    private Boolean requiresManualReview;
    private UseRestriction restriction;

    public SampleSet() {
        this.id = null;
        this.owner = null;
        this.requiresManualReview = null;
        this.restriction = null;
    }

    public SampleSet(String id, String owner, Boolean requiresManualReview, UseRestriction restriction) {
        this.id = id;
        this.owner = owner;
        this.requiresManualReview = requiresManualReview;
        this.restriction = restriction;
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public Boolean getRequiresManualReview() { return requiresManualReview; }
    public UseRestriction getRestriction() { return restriction; }

    public void setId(String id) { this.id = id; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setRequiresManualReview(Boolean requiresManualReview) {
        this.requiresManualReview = requiresManualReview;
    }
    public void setUseRestriction(UseRestriction useRestriction) { this.restriction = useRestriction; }

    public String toString() { return String.format("%s \"%s\"", id, owner); }

    public int hashCode() { return id != null ? id.hashCode() : 13; }

    public boolean equals(Object o) {
        if(!(o instanceof SampleSet)) { return false; }
        SampleSet ss = (SampleSet)o;
        // test == equality, to catch the situation where they're both null.
        return ss.id == id || ss.id.equals(id);
    }
}
