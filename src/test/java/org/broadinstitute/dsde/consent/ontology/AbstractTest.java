package org.broadinstitute.dsde.consent.ontology;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.common.io.*;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.datause.ontologies.OntologyList;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;


/**
 * Created by SantiagoSaucedo on 4/18/2016.
 */
public class AbstractTest {


    public static OntologyList getOntologyListMock() throws GeneralSecurityException, IOException {
        return new OntologyList(getStorageServiceMock());
    }

    public static StoreOntologyService getStorageServiceMock() throws GeneralSecurityException, IOException {
        final GCSStore storeMock = Mockito.mock(GCSStore.class);
        URL urlDiseasesOntologyFile = Thread.currentThread().getContextClassLoader().getResource("diseases.owl");
        URL urlDataUseOntologyFile = Thread.currentThread().getContextClassLoader().getResource("data-use.owl");
        URL urlOntologyConfiguration = Thread.currentThread().getContextClassLoader().getResource("ontologyConf.json");

        HttpTransport DataUseTransportMock = createInputStreamHttpTransportMock(urlDataUseOntologyFile);
        HttpTransport ConfigurationFileTransportMock = createInputStreamHttpTransportMock(urlOntologyConfiguration);
        HttpTransport DiseaseTransportMock = createInputStreamHttpTransportMock(urlDiseasesOntologyFile);

        Mockito.when(storeMock.getDocument("file1CloudURI")).thenReturn(DiseaseTransportMock.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL).execute());
        Mockito.when(storeMock.getDocument("file2CloudURI")).thenReturn(DataUseTransportMock.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL).execute());
        Mockito.when(storeMock.getStorageDocument(Mockito.anyString())).thenReturn(ConfigurationFileTransportMock.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL).execute());
        return new StoreOntologyService(storeMock, "AnyString", "AnyString");


    }

    private static HttpTransport createInputStreamHttpTransportMock(URL resourcePath) {
        HttpTransport diseaseTransportMock = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse result = new MockLowLevelHttpResponse();
                        result.setContent(Resources.asByteSource(resourcePath).openStream());
                        return result;
                    }
                };
            }
        };
        return diseaseTransportMock;
    }
}