package org.broadinstitute.dsp.ontology.factories

import org.broadinstitute.dsp.ontology.models.JsonProtocols
import spray.json._
import DefaultJsonProtocol._
import org.broadinstitute.dsp.ontology.models.DataUseModels._

object DataUseFactory {
    val generalUse: String = {
        val dataUse: DataUse = dataUseBuilder(generalUse = Some(true))
        implicit val dataUseFormat: JsonProtocols.DataUseFormat.type = JsonProtocols.DataUseFormat

        dataUse.toJson.compactPrint
    }

    val hmbResearch: String = {
        val dataUse: DataUse = dataUseBuilder(hmbResearch = Some(true))
        implicit val dataUseFormat: JsonProtocols.DataUseFormat.type = JsonProtocols.DataUseFormat

        dataUse.toJson.compactPrint
    }

    val notGeneralHmb: String = {
        val dataUse: DataUse = dataUseBuilder(generalUse = Some(false), hmbResearch = Some(true))
        implicit val dataUseFormat: JsonProtocols.DataUseFormat.type = JsonProtocols.DataUseFormat

        dataUse.toJson.compactPrint
    }
}