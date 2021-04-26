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
      GroupedScenario("Match: v1: DOID_162") {
        exec(
          Requests.matchV1(MatchBodyFactory.v1_162)
            .check(jsonPath("$.result").find.is("true"))
        )
      },
      GroupedScenario("Match: v1: Mismatch"){
        exec(
          Requests.matchV1(MatchBodyFactory.v1_EVPurpose_NonEVConsent)
            .check(jsonPath("$.result").find.is("false"))
        )
      },
      GroupedScenario("Match: v2: GeneralUse"){
        exec(
          Requests.matchV2(MatchBodyFactory.v2GeneralUse)
            .check(jsonPath("$.result").find.is("true"))
        )
      }
    )
  )

}
