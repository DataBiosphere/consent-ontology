package org.broadinstitute.dsp.ontology.models

import spray.json._
import DefaultJsonProtocol._

import org.broadinstitute.dsp.ontology.models.DataUseModels._

object JsonProtocols extends DefaultJsonProtocol {
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

        def read(value: JsValue) = {
            dataUseBuilder()
        }
    }
}