package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef._
import org.broadinstitute.dsp.ontology.requests.Requests

class StatusScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      scenario("Status Scenario").exec(Requests.statusRequest)
    )
  )

}
