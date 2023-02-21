package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.scenarios.GroupedScenarios._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.factories.MatchBodyFactory

class MatchScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      GroupedScenario("Match: v2: GeneralUse"){
        exec(
          Requests.matchV2(MatchBodyFactory.v2GeneralUse)
            .check(jsonPath("$.result").find.is("true"))
        )
      }
    )
  )

}
