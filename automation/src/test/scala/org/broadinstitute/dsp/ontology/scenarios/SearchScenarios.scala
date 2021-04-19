package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.scenarios.GroupedScenarios._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.models.SearchModels._

class SearchScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      GroupedScenario("Search 162") {
        exec(
          Requests.searchRequest(SearchUrl.URL_162)
            .check(jsonPath("$[0].id").find.is(SearchUrl.URL_162))
        )
      },
      GroupedScenario("Search 602") {
        exec(
          Requests.searchRequest(SearchUrl.URL_602)
            .check(jsonPath("$[0].id").find.is(SearchUrl.URL_602))
        )
      }
    )
  )

}
