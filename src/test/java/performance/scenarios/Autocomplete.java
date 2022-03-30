package performance.scenarios;

import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import java.util.List;
import performance.Endpoints;

public class Autocomplete implements Endpoints {

  public List<ScenarioBuilder> scenarios = List.of(
    scenario("Autocomplete: Cancer").exec(autocomplete("cancer").check(status().is(HttpStatusCodes.STATUS_CODE_OK))),
    scenario("Autocomplete: Diabetes").exec(autocomplete("diabetes").check(status().is(HttpStatusCodes.STATUS_CODE_OK))),
    scenario("Autocomplete: Apnea").exec(autocomplete("apnea").check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
  );

}
