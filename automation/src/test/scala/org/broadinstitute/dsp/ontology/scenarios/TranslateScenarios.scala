package org.broadinstitute.dsp.ontology.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.TestRunner
import org.broadinstitute.dsp.ontology.requests.Requests
import org.broadinstitute.dsp.ontology.factories.DataUseFactory
import org.broadinstitute.dsp.ontology.models.DataUseModels._

class TranslateScenarios extends Simulation with TestRunner {

  runScenarios(
    List(
      scenario("Translate: DataSet: General Use").exec(
        Requests.dataUseTranslateFor(Translations.DATASET, DataUseFactory.generalUse)
        .check(substring(DataUseResponses.GRU).exists)
      ),
      scenario("Translate: DataSet: Manual Review").exec(
        Requests.dataUseTranslateFor(Translations.DATASET, DataUseFactory.manualReview)
        .check(substring(DataUseResponses.ManualReview).exists)
      ),
      scenario("Translate: Purpose: General Use").exec(
        Requests.dataUseTranslateFor(Translations.PURPOSE, DataUseFactory.generalUse)
        .check(substring(DataUseResponses.GRU).exists)
      ),
      scenario("Translate: Purpose: Manual Review").exec(
        Requests.dataUseTranslateFor(Translations.PURPOSE, DataUseFactory.manualReview)
        .check(substring(DataUseResponses.ManualReview))
      )
    )
  )

}
