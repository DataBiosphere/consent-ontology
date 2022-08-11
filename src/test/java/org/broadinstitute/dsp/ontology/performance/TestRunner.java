package org.broadinstitute.dsp.ontology.performance;


import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.broadinstitute.dsp.ontology.performance.scenarios.Autocomplete;
import org.broadinstitute.dsp.ontology.performance.scenarios.DataUseSchema;
import org.broadinstitute.dsp.ontology.performance.scenarios.DataUseTranslate;
import org.broadinstitute.dsp.ontology.performance.scenarios.Matching;


/**
 * This is the main entry point for integration tests
 */
@SuppressWarnings("unused")
public class TestRunner extends Simulation {

  private final TestConfig config = new TestConfig();

  // Scenario Categories:
  private final Autocomplete autocomplete = new Autocomplete();
  private final DataUseSchema dataUseSchema = new DataUseSchema();
  private final DataUseTranslate dataUseTranslate = new DataUseTranslate();
  private final Matching matching = new Matching();

  private final HttpProtocolBuilder protocol = http
      .baseUrl(config.getBaseUrl())
      .acceptHeader("application/json")
      .doNotTrackHeader("1")
      .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  {
    setUp(
      Stream.of(
        autocomplete.scenarios,
        dataUseSchema.scenarios,
        dataUseTranslate.scenarios,
        matching.scenarios)
        .flatMap(List::stream)
        .map(scn -> scn.injectOpen(atOnceUsers(1)))
        .collect(Collectors.toList())
    ).protocols(protocol);
  }
}
