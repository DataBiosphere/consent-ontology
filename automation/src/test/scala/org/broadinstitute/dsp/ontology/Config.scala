package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._

object Config {

  val defaultUsers: Int = 1
  val defaultPause: FiniteDuration = 1 second
  val defaultUserAgent: String = "Gatling Client"
  val plainTextHeader: Map[String, String] = Map("Accept" -> "text/plain")
  val jsonHeader: Map[String, String] = Map("Accept" -> "application/json")

  // TODO: Handle environment
  val defaultHttpProtocol: HttpProtocolBuilder = {
    http
      .baseUrl("https://consent-ontology.dsde-dev.broadinstitute.org")
      .userAgentHeader(Config.defaultUserAgent)
  }

}
