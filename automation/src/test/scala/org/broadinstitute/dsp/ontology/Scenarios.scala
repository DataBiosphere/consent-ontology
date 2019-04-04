package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder

object Scenarios {

  // TODO: Config?
  val defaultUsers: Int = 1

  // TODO: Should be env specific
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://consent-ontology.dsde-dev.broadinstitute.org")
    .acceptHeader("application/json")
    .acceptHeader("text/plain")
    .userAgentHeader("Gatling Client")

  val statusScenario: ScenarioBuilder = scenario("Status Scenario")
    .exec(Requests.statusRequest)

  val defaultScenarios: List[ScenarioBuilder] = List(statusScenario)

}
