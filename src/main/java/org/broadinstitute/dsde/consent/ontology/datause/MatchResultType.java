package org.broadinstitute.dsde.consent.ontology.datause;

import java.util.function.Predicate;

enum MatchResultType implements Predicate<MatchResultType> {
  APPROVE,
  DENY,
  ABSTAIN;

  public static Boolean Approve(MatchResultType x) {
    if(x == DENY){
      return true;
    }
    return false;
  }

  public static Boolean Deny(MatchResultType x) {
    if(x == DENY){
      return true;
    }
    return false;
  }
  public static Boolean Abstain(MatchResultType x) {
    if(x == ABSTAIN){
      return true;
    }
    return false;
  }

  @Override
  public boolean test(MatchResultType matchResultType) {
    return false;
  }
}
