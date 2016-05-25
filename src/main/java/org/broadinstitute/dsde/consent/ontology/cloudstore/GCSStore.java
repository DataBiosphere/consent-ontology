package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.storage.StorageScopes;
import org.broadinstitute.dsde.consent.ontology.configurations.StoreConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GCSStore implements CloudStore {

    private final static Logger log = LoggerFactory.getLogger(GCSStore.class);
    private final static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private StoreConfiguration sConfig;
    private HttpRequestFactory requestFactory;

    public GCSStore(StoreConfiguration config) throws GeneralSecurityException, IOException {
        sConfig = config;
        requestFactory = HTTP_TRANSPORT.createRequestFactory(authorize());
    }


    /**
     * Authorizes the installed application to access user's protected data.
     */
    private GoogleCredential authorize() {
        GoogleCredential credential;
        try {
            credential = GoogleCredential.
                fromStream(new FileInputStream(sConfig.getPassword())).
                createScoped(Collections.singletonList(StorageScopes.DEVSTORAGE_FULL_CONTROL));
        } catch (Exception e) {
            log.error("Error on GCS Store initialization. Service won't work: " + e);
            throw new RuntimeException(e);
        }
        return credential;
    }


    @Override
    public HttpResponse getStorageDocument(String documentSuffix) throws IOException, GeneralSecurityException {
        HttpResponse response;
        HttpRequest request = buildHttpGetRequest(generateURLForDocument(documentSuffix));
        response = request.execute();
        return response;
    }

    private HttpRequest buildHttpGetRequest(GenericUrl url) throws IOException, GeneralSecurityException {
        return requestFactory.buildGetRequest(url);
    }

    private GenericUrl generateURLForDocument(String urlSuffix) {
        return new GenericUrl(sConfig.getEndpoint() + sConfig.getBucket() + "/"+urlSuffix);
    }

}
