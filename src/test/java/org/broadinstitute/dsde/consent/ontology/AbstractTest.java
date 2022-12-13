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
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.mockito.Mockito;
import org.parboiled.common.FileUtils;


/**
 * Created by SantiagoSaucedo on 4/18/2016.
 */
public class AbstractTest {

    protected static StoreOntologyService getStorageServiceMock() throws IOException {
        URL urlOntologyConfiguration = Resources.getResource("ontologyConf.json");
        Collection<URL> ontologyUrls = Arrays.asList(
            Resources.getResource("diseases.owl"),
            Resources.getResource("duo.owl"),
            Resources.getResource("data-use.owl"));
        Collection<String> ontologyStrings = ontologyUrls.stream().map(URL::toString).collect(Collectors.toList());

        StoreOntologyService storeServiceMock = Mockito.mock(StoreOntologyService.class);
        Mockito.when(storeServiceMock.retrieveConfigurationFile()).
            thenReturn(Resources.toString(urlOntologyConfiguration, Charset.defaultCharset()));
        Mockito.when(storeServiceMock.retrieveConfigurationKeys()).thenReturn(ontologyStrings);
        Mockito.when(storeServiceMock.retrieveOntologyURLs()).thenReturn(ontologyUrls);
        return storeServiceMock;
    }

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
      HttpRequest request = transport.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
      return request.execute();
    }

}