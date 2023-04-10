package org.broadinstitute.dsp.ontology.performance.scenarios;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import org.broadinstitute.dsp.ontology.performance.Endpoints;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

public class MatchingV3 implements Endpoints {

  private final String v3MatchPair = """
      {
        "purpose": {
          "generalUse": true
        },
        "consent": {
          "generalUse": true
        }
      }
      """;

  public List<ScenarioBuilder> tests = List.of(
      scenario("Match V3").exec(
          matchV3(v3MatchPair).check(status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5)
  );
}

