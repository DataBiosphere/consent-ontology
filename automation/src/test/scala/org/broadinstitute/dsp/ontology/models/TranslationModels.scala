package org.broadinstitute.dsp.ontology.models

import spray.json._
object TranslationModels {
  object Translation extends Enumeration {
      type Translation = String
      val DATASET = "dataset"
      val PURPOSE = "purpose"
  }

  object TranslationTypes extends Enumeration {
    type TranslationTypes = String
    val EVERYTHING: String = "everything"
    val AND: String = "and"
    val NOT: String = "not"
    val OR: String = "or"
    val NAMED: String = "named"
  }

  case class DataUseTranslation(
    `type`: String,
    operands: Option[Seq[DataUseTranslation]] = None,
    operand: Option[DataUseTranslation] = None,
    name: Option[String] = None
  )

  def dataUseTranslationBuilder(
    `type`: String,
    operands: Option[Seq[DataUseTranslation]] = None,
    operand: Option[DataUseTranslation] = None,
    name: Option[String] = None
  ): DataUseTranslation = {
    DataUseTranslation(
      `type`,
      operands,
      operand,
      name
    )
  }
}
