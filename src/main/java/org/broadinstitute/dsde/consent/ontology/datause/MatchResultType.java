package org.broadinstitute.dsde.consent.ontology.datause;

public enum MatchResultType {
  APPROVE,
  DENY,
  ABSTAIN;

  public static Boolean Approve(MatchResultType x) { return x == APPROVE; }

  public static Boolean Deny(MatchResultType x) {
    return x == DENY;
  }

  public static Boolean Abstain(MatchResultType x) { return x == ABSTAIN; }

}
