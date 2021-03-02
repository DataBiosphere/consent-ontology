package org.broadinstitute.dsp.ontology.models

import spray.json._
import DefaultJsonProtocol._

import org.broadinstitute.dsp.ontology.models.DataUseModels._
import org.broadinstitute.dsp.ontology.models.TranslationModels._
import org.broadinstitute.dsp.ontology.models.MatchModels._

object JsonProtocols extends DefaultJsonProtocol {
    implicit val dataUseTranslationFormat: RootJsonFormat[DataUseTranslation] = rootFormat(lazyFormat(jsonFormat4(DataUseTranslation)))
    implicit val v1MatchBodyFormat: JsonFormat[V1MatchBody] = jsonFormat2(V1MatchBody)
    implicit val v2MatchBodyFormat: JsonFormat[V2MatchBody] = jsonFormat2(V2MatchBody)

    implicit object DataUseFormat extends JsonFormat[DataUse] {
        def write(dataUse: DataUse) = {
            var map = collection.mutable.Map[String, JsValue]()
            dataUse.getClass.getDeclaredFields
                .foreach { f =>
                    f.setAccessible(true)
                    f.get(dataUse) match {
                        case Some(x: Boolean) => map += f.getName -> x.toJson
                        case Some(y: String) => map += f.getName -> y.toJson
                        case Some(l: Long) => map += f.getName -> l.toJson
                        case Some(i: Int) => map += f.getName -> i.toJson
                        case _ =>
                    }
                }

            JsObject(map.toMap)
        }

        def read(value: JsValue) = ???
    }
}
