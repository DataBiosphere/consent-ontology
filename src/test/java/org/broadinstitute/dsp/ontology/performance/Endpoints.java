package org.broadinstitute.dsp.ontology.performance;


import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpRequestActionBuilder;
import jakarta.ws.rs.core.MediaType;

/**
 * This interface defines all possible endpoints in the Ontology system. Some endpoints require
 * different accept/content-type headers so define those here. Each endpoint defined should cover a
 * single use case. In the event of APIs having multiple inputs, there should be explicit methods
 * for each variation.
 */
public interface Endpoints {

  default HttpRequestActionBuilder autocomplete(String term) {
    return
        http(String.format("Autocomplete: %s", term))
            .get(String.format("/autocomplete?q=%s", term))
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder dataUseSchema() {
    return
        http("Data Use Schema")
            .get("/schemas/data-use")
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder dataUseTranslateConsent(String json) {
    return
        http("Translate Consent DataUse to UseRestriction")
            .post("/schemas/data-use/consent/translate")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder dataUseTranslateDAR(String json) {
    return
        http("Translate DAR DataUse to UseRestriction")
            .post("/schemas/data-use/dar/translate")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder dataUseTranslateFor(String translateFor, String json) {
    return
        http(String.format("Translate For: %s", translateFor))
            .post(String.format("/translate?for=%s", translateFor))
            .body(StringBody(json))
            .header("Accept", MediaType.TEXT_PLAIN);
  }

  default HttpRequestActionBuilder dataUseTranslateSummary(String json) {
    return
        http("Translate Summary")
            .post("/translate/summary")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder matchV1(String json) {
    return
        http("Match V1")
            .post("/match/v1")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder matchV2(String json) {
    return
        http("Match V2")
            .post("/match/v2")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder matchV3(String json) {
    return
        http("Match V3")
            .post("/match/v3")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder search(String term) {
    return
        http("Search")
            .get(String.format("/search?id=%s", term))
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder liveness() {
    return
        http("Liveness")
            .get("/liveness")
            .header("Accept", MediaType.TEXT_PLAIN);
  }

  default HttpRequestActionBuilder systemStatus() {
    return
        http("Status")
            .get("/status")
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder systemVersion() {
    return
        http("Version")
            .get("/version")
            .header("Accept", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder validateUseRestriction(String json) {
    return
        http("Validate Use Restriction")
            .post("/validate/userestriction")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON);
  }

  default HttpRequestActionBuilder validateDataUseV3(String json) {
    return
        http("Validate Data Use")
            .post("/data-use/v3")
            .body(StringBody(json))
            .header("Accept", MediaType.APPLICATION_JSON)
            .header("Content-Type", MediaType.APPLICATION_JSON);
  }

}
