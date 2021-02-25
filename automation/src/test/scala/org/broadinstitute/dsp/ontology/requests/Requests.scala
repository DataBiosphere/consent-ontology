package org.broadinstitute.dsp.ontology.requests

import java.net.URLEncoder

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.request.builder._
import io.gatling.http.Predef._
import org.broadinstitute.dsp.ontology.TestConfig

object Requests {

  val rootRequest: ChainBuilder = exec(
    http("Root URL")
      .get("/")
      .check(status.is(session => 200))
  )

  // 'cancer' is a good example
  def autocomplete(term: String): ChainBuilder = exec(
    http(s"Autocomplete: $term")
      .get(s"/autocomplete?q=${encode(term)}")
      .headers(TestConfig.jsonHeader)
      .check(status.is(session => 200))
  )

  object Schemas {
    val dataUseSchema: ChainBuilder = exec(
      http("Data Use Schema")
        .get("/schemas/data-use")
        .headers(TestConfig.jsonHeader)
        .check(status.is(session => 200))
    )

    // '{ "generalUse": true }' is a good example
    def dataUseTranslateConsent(json: String): HttpRequestBuilder = {
      http("Translate Consent")
        .post("/schemas/data-use/consent/translate")
        .headers(TestConfig.jsonHeader)
        .body(StringBody(json))
        .asJson
        .check(status.is(session => 200))
    }

    // '{ "generalUse": true }' is a good example
    def dataUseTranslateDAR(json: String): HttpRequestBuilder = {
      http("Translate DAR")
        .post("/schemas/data-use/dar/translate")
        .headers(TestConfig.jsonHeader)
        .body(StringBody(json))
        .asJson
        .check(status.is(session => 200))
    }
  }

  // ('dataset' || 'purpose') and '{ "generalUse": true }' are good examples
  def dataUseTranslateFor(whatFor: String, json: String): HttpRequestBuilder = {
    http("Translate")
      .post(s"/translate?for=$whatFor")
      .headers(TestConfig.plainTextHeader)
      .body(StringBody(json))
      .asJson
      .check(status.is(session => 200))
  }

  // '{ "purpose": { "type": "everything" }, "consent": { "type": "everything" } }' is a good example
  def matchV1(json: String): ChainBuilder = {
    exec(http("Match V1")
      .post(s"/matchv1")
      .headers(TestConfig.jsonHeader)
      .body(StringBody(json))
      .asJson
      .check(status.is(session => 200)))
  }

  // '{ "purpose": {"hmbResearch": true}, "consent": {"generalUse": true} }' is a good example
  def matchV2(json: String): ChainBuilder = {
    exec(http("Match V2")
      .post(s"/match/v2")
      .headers(TestConfig.jsonHeader)
      .body(StringBody(json))
      .asJson
      .check(status.is(session => 200)))
  }

  // 'http://purl.obolibrary.org/obo/DOID_162' is a good example
  def searchRequest(term: String): ChainBuilder = {
    exec(http(s"Search: $term")
      .get(s"/search?id=${encode(term)}")
      .headers(TestConfig.jsonHeader)
      .check(status.is(session => 200)))
  }

  val statusRequest: ChainBuilder = exec(
    http("Status Request")
      .get("/status")
      .headers(TestConfig.jsonHeader)
      .check(status.is(session => 200))
  )

  val versionRequest: ChainBuilder = exec(
    http("Version Request")
      .get("/version")
      .headers(TestConfig.jsonHeader)
      .check(status.is(session => 200))
  )

  // '{ "type": "everything" }' is a good example
  def validate(json: String): ChainBuilder = {
    exec(
      http("Validate Use Restriction")
        .post("/validate/userestriction")
        .headers(TestConfig.jsonHeader)
        .form(json)
        .check(status.is(session => 200))
    )
  }

  private def encode(term: String): String = {
    URLEncoder.encode(term, "UTF-8")
  }

}
