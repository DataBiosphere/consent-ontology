package org.broadinstitute.dsp.ontology

import scala.concurrent.duration._


object Config {

  val defaultUsers: Int = 1
  val defaultPause: FiniteDuration = 1 second
  val defaultClient: String = "Gatling Client"
  val applicationJson: String = "application/json"
  val plainText: String = "text/plain"

}
