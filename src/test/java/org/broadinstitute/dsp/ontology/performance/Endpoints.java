package org.broadinstitute.dsp.ontology.performance;


import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpRequestActionBuilder;
import java.util.Map;
import javax.ws.rs.core.MediaType;

public interface Endpoints {

  Map<CharSequence, String> jsonHeaders = Map.of("Accept", MediaType.APPLICATION_JSON_TYPE.getType());
  Map<CharSequence, String> plainTextHeaders = Map.of("Accept", MediaType.TEXT_PLAIN_TYPE.getType());

  default HttpRequestActionBuilder autocomplete(String term) {
    return
        http(String.format("Autocomplete: %s", term))
            .get(String.format("/autocomplete?q=%s", term))
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder dataUseSchema() {
    return
        http("Data Use Schema")
            .get("/schemas/data-use")
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder dataUseTranslateConsent(String json) {
    return
        http("Translate Consent")
            .post("/schemas/data-use/consent/translate")
            .body(StringBody(json))
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder dataUseTranslateDAR(String json) {
    return
        http("Translate DAR")
            .post("/schemas/data-use/dar/translate")
            .body(StringBody(json))
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder dataUseTranslateFor(String translateFor, String json) {
    return
        http(String.format("Translate For: %s", translateFor))
            .post(String.format("/translate?for%s", translateFor))
            .body(StringBody(json))
            .headers(plainTextHeaders);
  }

  default HttpRequestActionBuilder matchV1(String json) {
    return
        http("Match V1")
            .post("/match/v1")
            .body(StringBody(json))
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder matchV2(String json) {
    return
        http("Match V2")
            .post("/match/v2")
            .body(StringBody(json))
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder search(String term) {
    return
        http("Search")
            .get(String.format("/search?id=%s", term))
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder systemStatus() {
    return
        http("Status")
            .get("/status")
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder systemVersion() {
    return
        http("Version")
            .get("/version")
            .headers(jsonHeaders);
  }

  default HttpRequestActionBuilder validate(String json) {
    return
        http("Validate Use Restriction")
            .post("/validate/userestriction")
            .body(StringBody(json))
            .headers(jsonHeaders);
  }

}
