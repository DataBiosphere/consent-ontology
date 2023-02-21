package org.broadinstitute.dsp.ontology.models

import org.broadinstitute.dsp.ontology.models.DataUseModels._

object MatchModels {
  case class V2MatchBody(purpose: DataUse, consent: DataUse)
}
