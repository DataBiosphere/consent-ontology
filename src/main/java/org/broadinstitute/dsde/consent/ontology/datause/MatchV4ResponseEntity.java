package org.broadinstitute.dsde.consent.ontology.datause;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV4;

public class MatchV4ResponseEntity {

  private final MatchResultType result;
  private final DataUseMatchPairV4 matchPair;
  private final List<String> rationale;

  public MatchV4ResponseEntity(MatchResultType result, DataUseMatchPairV4 matchPair,
      List<String> rationale) {
    this.result = result;
    this.matchPair = matchPair;
    this.rationale = rationale.stream().distinct().toList();
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

  public DataUseMatchPairV4 getMatchPair() {
    return this.matchPair;
  }
  public List<String> getRationale() {
    return this.rationale;
  }
}
