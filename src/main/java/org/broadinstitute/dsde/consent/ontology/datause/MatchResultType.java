package org.broadinstitute.dsde.consent.ontology.datause;

import java.util.function.Predicate;
import java.util.regex.MatchResult;

enum MatchResultType implements Predicate<MatchResultType> {
  APPROVE,
  DENY,
  ABSTAIN;

  public static Boolean Approve(MatchResultType x) { return x == APPROVE; }

  public static Boolean Deny(MatchResultType x) {
    return x == DENY;
  }

  public static Boolean Abstain(MatchResultType x) { return x == ABSTAIN; }

  @Override
  public boolean test(MatchResultType matchResultType) {
    return false;
  }
}
