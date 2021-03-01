package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.factories.DataUseFactory
import org.broadinstitute.dsp.ontology.models.DataUseModels._
import org.broadinstitute.dsp.ontology.models.TranslationModels._

class TranslateScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      scenario("Translate: DataSet: General Use").exec(
        Requests.dataUseTranslateFor(Translation.DATASET, DataUseFactory.generalUse)
        .check(substring(DataUseResponses.GRU_BRACKETS).exists)
      ),
      scenario("Translate: DataSet: Manual Review").exec(
        Requests.dataUseTranslateFor(Translation.DATASET, DataUseFactory.manualReview)
        .check(substring(DataUseResponses.MANUAL_REVIEW).exists)
      ),
      scenario("Translate: Purpose: General Use").exec(
        Requests.dataUseTranslateFor(Translation.PURPOSE, DataUseFactory.generalUse)
        .check(substring(DataUseResponses.GRU_BRACKETS).exists)
      ),
      scenario("Translate: Purpose: Manual Review").exec(
        Requests.dataUseTranslateFor(Translation.PURPOSE, DataUseFactory.manualReview)
        .check(substring(DataUseResponses.MANUAL_REVIEW))
      ),
      scenario("Translate: Summary: General Use").exec(
        Requests.dataUseTranslateSummary(DataUseFactory.generalUse)
        .check(substring(DataUseResponses.GRU_CODE))
      ),
      scenario("Translate: Summary: Manual Review").exec(
        Requests.dataUseTranslateSummary(DataUseFactory.manualReview)
        .check(substring(DataUseResponses.MANUAL_REVIEW).exists)
      )
    )
  )

}
