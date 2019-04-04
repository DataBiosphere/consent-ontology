package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef._
import org.broadinstitute.dsp.ontology.Scenarios._

class DefaultScenarios extends Simulation {

  setUp(
    defaultScenarios.map(scn =>
      scn
        .pause(Config.defaultPause)
        .inject(atOnceUsers(Config.defaultUsers))
    )
  ).protocols(httpProtocol)

}
