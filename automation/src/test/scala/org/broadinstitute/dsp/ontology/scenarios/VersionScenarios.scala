package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import org.broadinstitute.dsp.ontology.scenarios.GroupedScenarios._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests

class VersionScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      GroupedScenario("Version Scenario") { exec(Requests.versionRequest) }
    )
  )

}
