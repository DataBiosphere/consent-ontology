package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests

class AutocompleteScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      scenario("Autocomplete: Cancer").exec(Requests.autocomplete("cancer")),
      scenario("Autocomplete: Diabetes").exec(Requests.autocomplete("diabetes")),
      scenario("Autocomplete: Apnea").exec(Requests.autocomplete("apnea")),
    )
  )

}
