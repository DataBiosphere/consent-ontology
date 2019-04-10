package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef.{Simulation, atOnceUsers, _}
import io.gatling.core.structure.ScenarioBuilder


trait TestRunner extends Simulation {

  def runScenarios(scenarios: List[ScenarioBuilder]): SetUp = {
    setUp(
      scenarios.map { scn =>
        scn
          .pause(TestConfig.defaultPause)
          .inject(atOnceUsers(TestConfig.defaultUsers))
      }
    ).protocols(TestConfig.defaultHttpProtocol)
  }

}
