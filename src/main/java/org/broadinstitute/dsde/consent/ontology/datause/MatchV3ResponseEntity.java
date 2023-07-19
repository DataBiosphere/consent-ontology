package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;

public class MatchV3ResponseEntity {

  private final MatchResultType result;
  private final DataUseMatchPairV3 matchPair;
  private final List<String> rationale;

  public MatchV3ResponseEntity(MatchResultType result, DataUseMatchPairV3 matchPair,
      List<String> rationale) {
    this.result = result;
    this.matchPair = matchPair;
    this.rationale = rationale;
  }

  public ImmutableMap<String, Object> get() {
    return ImmutableMap.of(
        "result", this.result,
        "matchPair", this.matchPair,
        "rationale", this.rationale);
  }

  public MatchResultType getResult() {
    return this.result;
  }

  public DataUseMatchPairV3 getMatchPair() {
    return this.matchPair;
  }
  public List<String> getRationale() {
    return this.rationale;
  }
}
