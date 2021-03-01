package org.broadinstitute.dsp.ontology.models

import org.broadinstitute.dsp.ontology.models.TranslationModels._
import org.broadinstitute.dsp.ontology.models.DataUseModels._

object MatchModels {
  case class V1MatchBody(purpose: DataUseTranslation, consent: DataUseTranslation)

  case class V2MatchBody(purpose: DataUse, consent: DataUse)
}
