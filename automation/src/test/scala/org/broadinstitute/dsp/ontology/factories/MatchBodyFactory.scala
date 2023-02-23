package org.broadinstitute.dsp.ontology.factories

import spray.json._
import DefaultJsonProtocol._
import org.broadinstitute.dsp.ontology.models.JsonProtocols
import org.broadinstitute.dsp.ontology.models.MatchModels._
import org.broadinstitute.dsp.ontology.models.TranslationModels._
import org.broadinstitute.dsp.ontology.models.DataUseModels._
import org.broadinstitute.dsp.ontology.models.SearchModels._
import javax.xml.crypto.Data

object MatchBodyFactory {

  def v2BodyString(v2MatchBody: V2MatchBody): String = {
    implicit val v2MatchBodyFormat: JsonFormat[V2MatchBody] = JsonProtocols.v2MatchBodyFormat
    implicit val dataUseFormat: JsonProtocols.DataUseFormat.type = JsonProtocols.DataUseFormat

    s"""{"purpose": ${v2MatchBody.purpose.toJson.compactPrint},"consent": ${v2MatchBody.consent.toJson.compactPrint}}"""
  }

  val v2GeneralUse: String = v2BodyString(
    V2MatchBody(
      purpose = DataUseFactory.Objects.generalUse,
      consent = DataUseFactory.Objects.generalUse
    )
  )
}
