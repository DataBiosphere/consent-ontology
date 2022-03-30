package performance;


import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.google.api.client.http.HttpStatusCodes;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;


/**
 * This is the main entry point for integration tests
 */
@SuppressWarnings("unused")
public class TestRunner extends Simulation implements Endpoints {

  private final TestConfig config = new TestConfig();

  private final HttpProtocolBuilder protocol = http
      .baseUrl(config.getBaseUrl())
      .acceptHeader("application/json")
      .doNotTrackHeader("1")
      .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  private final ScenarioBuilder scn = scenario("Autocomplete Simulation")
      .exec(autocomplete("cancer").check(status().is(HttpStatusCodes.STATUS_CODE_OK)))
      .pause(config.getPause());

  {
    setUp(
        scn.injectOpen(atOnceUsers(1))
    ).protocols(protocol);
  }
}
