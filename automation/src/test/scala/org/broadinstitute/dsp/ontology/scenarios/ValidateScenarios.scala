package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.models.TranslationModels._
import org.broadinstitute.dsp.ontology.factories.TranslationsFactory

class ValidateScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      scenario("Validate Use Restriction Everything").exec(
        Requests.validate(TranslationsFactory.everything)
        .check(jsonPath("$.valid").find.is("true"))
      ),
      scenario("Validate Use Restriction hmbResearch").exec(
        Requests.validate(TranslationsFactory.hmbResearch)
        .check(jsonPath("$.valid").find.is("true"))
      )
    )
  )

}
