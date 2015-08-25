package org.broadinstitute.dsde.consent.ontology.datause.models;

import com.google.common.base.Objects;

public class Consent {

    private String id, owner;
    private UseRestriction restriction;

    public Consent() {
        this.id = null;
        this.owner = null;
        this.restriction = null;
    }

    public Consent(String id, String owner, UseRestriction restriction) {
        this.id = id;
        this.owner = owner;
        this.restriction = restriction;
    }

    public String getId() { return id; }
    public String getOwner() { return owner; }
    public UseRestriction getRestriction() { return restriction; }

    public void setId(String id) { this.id = id; }
    public void setOwner(String owner) { this.owner = owner; }
    public void setUseRestriction(UseRestriction useRestriction) { this.restriction = useRestriction; }

    public String toString() { return String.format("%s \"%s\"", id, owner); }

    public int hashCode() {
        return Objects.hashCode(this.id, this.owner, this.restriction);
    }

    public boolean equals(Object o) {
        if(!(o instanceof Consent)) { return false; }
        Consent otherConsent = (Consent) o;
        return Objects.equal(this.getId(), otherConsent.getId()) &&
                Objects.equal(this.getOwner(), otherConsent.getOwner()) &&
                Objects.equal(this.getRestriction(), otherConsent.getRestriction());
    }
}
