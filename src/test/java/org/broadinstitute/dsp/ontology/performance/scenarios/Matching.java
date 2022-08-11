package org.broadinstitute.dsp.ontology.performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsp.ontology.performance.Endpoints;

public class Matching implements Endpoints {

  private final String generalUse = new Gson().toJson(new DataUseBuilder().setGeneralUse(true).build());

  private final String useRestriction = """
                                        {
                                          "purpose": {
                                            "type": "named",
                                            "name": "http://purl.obolibrary.org/obo/DOID_162"
                                          },
                                          "consent": {
                                            "type": "named",
                                            "name": "http://purl.obolibrary.org/obo/DOID_162"
                                          }
                                        }
                                        """;

  public List<ScenarioBuilder> scenarios = List.of(
    scenario("Match V1").exec(matchV1(useRestriction).check(status().is(HttpStatusCodes.STATUS_CODE_OK))),
    scenario("Match V2").exec(matchV2(generalUse).check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
  );
}
