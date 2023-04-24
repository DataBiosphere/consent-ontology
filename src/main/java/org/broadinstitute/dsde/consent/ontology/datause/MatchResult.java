package org.broadinstitute.dsde.consent.ontology.datause;

import java.util.List;

public class MatchResult {

  private final MatchResultType matchResultType;
  private final List<String> message;

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
}
