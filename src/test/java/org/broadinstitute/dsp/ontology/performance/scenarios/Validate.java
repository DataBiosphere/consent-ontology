package org.broadinstitute.dsp.ontology.performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import org.broadinstitute.dsp.ontology.performance.Endpoints;

public class Validate implements Endpoints {

  String useRestriction = """
      {
        "type": "named",
        "name": "http://purl.obolibrary.org/obo/DOID_162"
      }
      """;

  public List<ScenarioBuilder> tests = List.of(
      scenario("Validate Use Restriction").exec(validateUseRestriction(useRestriction).check(status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5)
  );
}
