package org.broadinstitute.dsp.ontology.factories

import org.broadinstitute.dsp.ontology.models.JsonProtocols
import spray.json._
import DefaultJsonProtocol._
import org.broadinstitute.dsp.ontology.models.DataUseModels._

object DataUseFactory {
    def getDataUseString(dataUse: DataUse): String = {
        implicit val dataUseFormat: JsonProtocols.DataUseFormat.type = JsonProtocols.DataUseFormat
        dataUse.toJson.compactPrint
    }

    val generalUse: String = getDataUseString(dataUseBuilder(generalUse = Some(true)))

    val hmbResearch: String = getDataUseString(dataUseBuilder(hmbResearch = Some(true)))

    val notGeneralHmb: String = getDataUseString(dataUseBuilder(generalUse = Some(false), hmbResearch = Some(true)))

    val manualReview: String = getDataUseString(dataUseBuilder(manualReview = Some(true)))
}
