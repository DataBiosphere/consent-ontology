package org.broadinstitute.dsde.consent.ontology.service;

import com.google.api.client.http.*;
import com.google.api.client.json.Json;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import org.broadinstitute.dsde.consent.ontology.cloudstore.CloudStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StoreOntologyServiceTest {

    private StoreOntologyService storeOntologyService;
    @Mock
    CloudStore store;
    @Mock
    MockLowLevelHttpResponse response;

    private static final String content = "{\"name\":\"http://www.broadinstitute.org/ontologies/DURPO/methods_research\"}";
    private static final String urls = "{\"http://www.broadinstitute.org/ontologies/DURPO/research_type\":\"http://www.broadinstitute.org/ontologies/DURPO/methods_research\"}";
    private static final String error = "{\"error:\":\"Not Found\"}";

    @Before
    public void setUpClass() {
        MockitoAnnotations.initMocks(this);
        storeOntologyService = new StoreOntologyService(store, "ontology", "ontology");
    }

    @Test
    public void testRetrieveConfigurationFile() throws Exception {
        HttpResponse response = getHttpResponse(content);
        when(store.getStorageDocument(Mockito.anyString())).thenReturn(response);
        String result = storeOntologyService.retrieveConfigurationFile();
        assertThat(result.contains(content));
    }

    @Test(expected = InternalError.class)
    public void testRetrieveConfigurationFileWithNullResponse() throws Exception {
        when(store.getStorageDocument(Mockito.anyString())).thenReturn(null);
        storeOntologyService.retrieveConfigurationFile();
    }

    @Test
    public void testRetrieveOntologyURLs() throws Exception {
        HttpResponse httpResponse = getHttpResponse(urls);
        when(store.getStorageDocument(Mockito.anyString())).thenReturn(httpResponse);
        List<URL> urls = (ArrayList)storeOntologyService.retrieveOntologyURLs();
        urls.get(0).equals(new URL("http://www.broadinstitute.org/ontologies/DURPO/research_type"));
    }

    @Test
    public void testRetrieveOntologyURLWithInvalidFormat() throws Exception {
        HttpResponse httpResponse = getHttpResponse(error);
        when(store.getStorageDocument(Mockito.anyString())).thenReturn(httpResponse);
        assertThat(storeOntologyService.retrieveOntologyURLs() == null);

    }


    private HttpResponse getHttpResponse(String content) throws IOException {
        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
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
