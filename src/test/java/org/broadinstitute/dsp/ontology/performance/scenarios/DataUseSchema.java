package org.broadinstitute.dsp.ontology.performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import org.broadinstitute.dsp.ontology.performance.Endpoints;

public class DataUseSchema implements Endpoints {

  public List<ScenarioBuilder> tests = List.of(
      scenario("Data Use Schema").exec(
          dataUseSchema().check(status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5)
  );

}
