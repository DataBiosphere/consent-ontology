package org.broadinstitute.dsp.ontology.performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsp.ontology.performance.Endpoints;

public class DataUseTranslate implements Endpoints {

  private final String generalUse = new Gson().toJson(
      new DataUseBuilder().setGeneralUse(true).build());
  public List<ScenarioBuilder> tests = List.of(
      scenario("Data Use Translation DAR: General Use").exec(
              dataUseTranslateDAR(generalUse).check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
          .pause(1, 5),
      scenario("Data Use Translation Consent: General Use").exec(
              dataUseTranslateConsent(generalUse).check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
          .pause(1, 5),
      scenario("Translate For Purpose: General Use").exec(
          dataUseTranslateFor("purpose", generalUse).check(
              status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5),
      scenario("Translate For Consent: General Use").exec(
          dataUseTranslateFor("dataset", generalUse).check(
              status().is(HttpStatusCodes.STATUS_CODE_OK))).pause(1, 5),
      scenario("Translate Summary: General Use").exec(
              dataUseTranslateSummary(generalUse).check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
          .pause(1, 5)
  );
}
