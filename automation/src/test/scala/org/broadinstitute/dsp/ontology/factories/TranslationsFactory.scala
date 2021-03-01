package org.broadinstitute.dsp.ontology.factories

import org.broadinstitute.dsp.ontology.models.JsonProtocols
import spray.json._
import DefaultJsonProtocol._
import org.broadinstitute.dsp.ontology.models.TranslationModels._
import org.broadinstitute.dsp.ontology.models.SearchModels._

object TranslationsFactory {
  object Objects {
    val everything: DataUseTranslation = dataUseTranslationBuilder(`type` = TranslationTypes.EVERYTHING)
    val doid162: DataUseTranslation = dataUseTranslationBuilder(`type` = TranslationTypes.NAMED, name = Some(SearchUrl.URL_162))
    val noGeneral: DataUseTranslation = dataUseTranslationBuilder(
        `type` = TranslationTypes.NAMED,
        name = Some(SearchUrl.NO_GENERAL)
      )
    val hmbResearch: DataUseTranslation = dataUseTranslationBuilder(
      `type` = TranslationTypes.OR,
      operands = Some(Seq(dataUseTranslationBuilder(
        `type` = TranslationTypes.NAMED,
        name = Some(SearchUrl.NO_GENERAL)
      ),
      everything
      ))
    )
  }

  def printSeq(seq: Seq[DataUseTranslation]): String = {
    val strSeq: Seq[String] = seq.map(op => getTranslationsString(op))
    strSeq.mkString(", ")
  }

  def getTranslationsString(translation: DataUseTranslation): String = {
    s"""{"type": "${translation.`type`}"${(translation.`type` match {
      case TranslationTypes.AND | TranslationTypes.OR => s""", "operands": [${printSeq(translation.operands.getOrElse(Seq(dataUseTranslationBuilder(`type` = TranslationTypes.EVERYTHING))))}]"""
      case TranslationTypes.NOT => s""", "operand": ${getTranslationsString(translation.operand.getOrElse(dataUseTranslationBuilder(`type` = TranslationTypes.EVERYTHING)))}"""
      case TranslationTypes.NAMED => s""", "name": "${translation.name.getOrElse("")}""""
      case _ => ""
    })}}"""
  }

  val everything: String = getTranslationsString(Objects.everything)
  val noGeneral: String = getTranslationsString(Objects.noGeneral)
  val hmbResearch: String = getTranslationsString(Objects.hmbResearch)
}
