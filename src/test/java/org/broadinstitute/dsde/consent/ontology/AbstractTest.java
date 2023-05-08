package org.broadinstitute.dsde.consent.ontology;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import java.io.IOException;


/**
 * Created by SantiagoSaucedo on 4/18/2016.
 */
public class AbstractTest {

  protected static HttpResponse getMockHttpResponse(String content) throws IOException {
    HttpTransport transport = new MockHttpTransport() {
      @Override
      public LowLevelHttpRequest buildRequest(String method, String url) {
        return new MockLowLevelHttpRequest() {
          @Override
          public LowLevelHttpResponse execute() {
            MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
            response.setStatusCode(HttpStatusCodes.STATUS_CODE_OK);
            response.setContentType(Json.MEDIA_TYPE);
            response.setContent(content);
            return response;
          }
        };
      }
    };
    HttpRequest request = transport.createRequestFactory()
        .buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
    return request.execute();
  }

}