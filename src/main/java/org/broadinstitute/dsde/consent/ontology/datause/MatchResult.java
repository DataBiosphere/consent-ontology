package org.broadinstitute.dsde.consent.ontology.datause;

import java.util.List;

public class MatchResult {

  private final MatchResultType matchResultType;
  private final List<String> message;
  private MatchResultType left;
  private List<String> right;

  private MatchResult(MatchResultType matchResultType, List<String> message) {
    this.matchResultType = matchResultType;
    this.message = message;
  }

  public MatchResultType getMatchResultType() {
    return matchResultType;
  }

  public List<String> getMessage() {
    return message;
  }

  public static MatchResult from(MatchResultType matchResultType,
      List<String> message) {
    return new MatchResult(matchResultType, message);
  }

  public MatchResultType getLeft() {
    return this.left;
  }

  public List<String> getRight() {
    return this.right;
  }

  public static MatchResultType isApprove() {
    return MatchResultType.APPROVE;
  }
  public static MatchResultType isDeny() {
    return MatchResultType.DENY;
  }
}
