package org.broadinstitute.dsde.consent.ontology.datause.models;

public interface UseRestrictionVisitor {

    void startChildren();
    void endChildren();
    boolean visit(UseRestriction r);

}
