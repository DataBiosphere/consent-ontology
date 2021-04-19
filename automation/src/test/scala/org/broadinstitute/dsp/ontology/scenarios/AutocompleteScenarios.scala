package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import org.broadinstitute.dsp.ontology.scenarios.GroupedScenarios._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests

class AutocompleteScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      GroupedScenario("Autocomplete: Cancer") { exec(Requests.autocomplete("cancer")) },
      GroupedScenario("Autocomplete: Diabetes") { exec(Requests.autocomplete("diabetes")) },
      GroupedScenario("Autocomplete: Apnea") { exec(Requests.autocomplete("apnea")) }
    )
  )

}
