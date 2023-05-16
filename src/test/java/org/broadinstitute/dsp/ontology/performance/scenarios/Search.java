package org.broadinstitute.dsp.ontology.performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import org.broadinstitute.dsp.ontology.performance.Endpoints;

public class Search implements Endpoints {

  public List<ScenarioBuilder> tests = List.of(
      scenario("Search: DOID_162").exec(
          search("DOID_162").check(status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5),
      scenario("Search: DOID_4").exec(
          search("DOID_4").check(status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5)
  );

}
