package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.factories.DataUseFactory

class SchemaScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      scenario("Schema: DataUse").exec(Requests.Schemas.dataUseSchema),
      scenario("Schema: Translate Consent General Use").exec(
        Requests.Schemas.dataUseTranslateConsent(DataUseFactory.generalUse)
        .check(jsonPath("$.type").find.is("everything"))
      ),
      scenario("Schema: Translate Consent hmbResearch").exec(
        Requests.Schemas.dataUseTranslateConsent(DataUseFactory.hmbResearch)
        .check(jsonPath("$.type").find.is("or"))
      ),
      scenario("Schema: Translate Consent hmbResearch Not General Use").exec(
        Requests.Schemas.dataUseTranslateConsent(DataUseFactory.notGeneralHmb)
      ),
      scenario("Schema: Translate Consent manualReview").exec(
        Requests.Schemas.dataUseTranslateConsent(DataUseFactory.manualReview)
      ),
      scenario("Schema: Translate DAR General Use").exec(
        Requests.Schemas.dataUseTranslateDAR(DataUseFactory.generalUse)
        .check(jsonPath("$.type").find.is("everything"))
      ),
      scenario("Schema: Translate DAR manualReview").exec(
        Requests.Schemas.dataUseTranslateDAR(DataUseFactory.manualReview)
        .check(jsonPath("$.type").find.is("and"))
        .check(jsonPath("$.operands[0].type").find.is("and"))
      )
    )
  )

}
