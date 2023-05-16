package org.broadinstitute.dsp.ontology.performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import org.broadinstitute.dsp.ontology.performance.Endpoints;

public class DataUseV3 implements Endpoints {

  String dataUseV3 = """
      {
      "generalUse": true,
      "diseaseRestrictions": ["test"],
      "hmbResearch": true,
      }
      """;

  public List<ScenarioBuilder> tests = List.of(
      scenario("Validate Data Use").exec(
              validateDataUseV3(dataUseV3).check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
          .pause(1, 5)
  );
}
