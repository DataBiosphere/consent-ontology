package org.broadinstitute.dsp.ontology

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

object Scenarios {

  // TODO: Config?
  val defaultUsers: Int = 1

  // TODO: Should be env specific
  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://consent-ontology.dsde-dev.broadinstitute.org")
    .acceptHeader("application/json")
    .userAgentHeader("Gatling Client")

  // TODO: Requests should be in a separate object
  val statusRequest: HttpRequestBuilder = http("Status Request")
    .get("/status")
    .check(status.is(session => 200))

  val statusScenario: ScenarioBuilder = scenario("Status Scenario")
    .exec(statusRequest)

  val scenarioList: List[ScenarioBuilder] = List(statusScenario)

}
