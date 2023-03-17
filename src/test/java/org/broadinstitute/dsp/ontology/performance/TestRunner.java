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
import org.broadinstitute.dsp.ontology.performance.scenarios.DataUseV3;
import org.broadinstitute.dsp.ontology.performance.scenarios.Matching;
import org.broadinstitute.dsp.ontology.performance.scenarios.Search;
import org.broadinstitute.dsp.ontology.performance.scenarios.Status;


/**
 * This is the main entry point for smoke/performance tests
 * Headers common to all requests are defined here in the protocol builder.
 */
public class TestRunner extends Simulation {

  private final TestConfig config = new TestConfig();

  private final HttpProtocolBuilder protocol = http
      .baseUrl(config.getBaseUrl())
      .header("X-App-ID", "DUOS")
      .doNotTrackHeader("1")
      .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0");

  {
    setUp(
        Stream.of(
                new Autocomplete().tests,
                new DataUseSchema().tests,
                new DataUseTranslate().tests,
                new Matching().tests,
                new Search().tests,
                new Status().tests,
                new DataUseV3().tests
            )
            .flatMap(List::stream)
            .map(scn -> scn.injectOpen(atOnceUsers(1)))
            .collect(Collectors.toList())
    ).protocols(protocol);
  }
}
