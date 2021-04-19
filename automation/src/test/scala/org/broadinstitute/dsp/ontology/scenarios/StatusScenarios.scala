package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import org.broadinstitute.dsp.ontology.scenarios.GroupedScenarios._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests

class StatusScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      GroupedScenario("Status Scenario") {
        exec(Requests.statusRequest)
      }
    )
  )

}
