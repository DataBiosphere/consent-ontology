package performance;


import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public interface Endpoints {

  default HttpRequestActionBuilder autocomplete(String term) {
    return
        http(String.format("Autocomplete: %s", term))
            .get(String.format("/autocomplete?q=%s", term));
  }
}
