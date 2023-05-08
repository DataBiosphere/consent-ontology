package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;

public class DataUseResponseEntity {

  private final MatchResultType result;
  private final DataUseMatchPairV3 matchPair;
  private final List<String> failureReasons;

  public DataUseResponseEntity(MatchResultType result, DataUseMatchPairV3 matchPair,
      List<String> failureReasons) {
    this.result = result;
    this.matchPair = matchPair;
    this.failureReasons = failureReasons;
  }

  public ImmutableMap<String, Object> get() {
    return ImmutableMap.of(
        "result", this.result,
        "matchPair", this.matchPair,
        "failureReasons", this.failureReasons);
  }

  public MatchResultType getResult() {
    return this.result;
  }

  public DataUseMatchPairV3 getMatchPair() {
    return this.matchPair;
  }

  public List<String> getFailureReasons() {
    return this.failureReasons;
  }
}
