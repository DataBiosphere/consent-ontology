package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import org.broadinstitute.dsp.ontology.requests.Requests

class DefaultScenarios extends Simulation {

  val defaultScenarios: List[ScenarioBuilder] = List(
    scenario("Status Scenario").exec(Requests.statusRequest)
  )

  setUp(
    defaultScenarios.map(scn =>
      scn
        .pause(Config.defaultPause)
        .inject(atOnceUsers(Config.defaultUsers))
    )
  ).protocols(Config.defaultHttpProtocol)

}
