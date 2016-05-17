package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import org.apache.log4j.Logger;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.StorageScopes;
import org.broadinstitute.dsde.consent.ontology.configurations.StoreConfiguration;

public class GCSStore implements CloudStore {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private StoreConfiguration sConfig;
    HttpRequestFactory requestFactory;
    GoogleCredential credential;

    protected Logger logger() {
        return Logger.getLogger("GCSStore");
    }

    public GCSStore(StoreConfiguration config) throws GeneralSecurityException, IOException {
        sConfig = config;
        credential = authorize();
        requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
    }


    /**
     * Authorizes the installed application to access user's protected data.
     */
    private GoogleCredential authorize() {
        GoogleCredential credential = new GoogleCredential();
        try {
            InputStream inputStream = new FileInputStream(sConfig.getPassword());
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
            String privateKeyPem = (String) clientSecrets.get("private_key");
            String clientEmail = (String) clientSecrets.get("client_email");
            String privateKeyId = (String) clientSecrets.get("private_key_id");
            File privateKeyPemFile = createKeyPemFile(privateKeyPem);
            credential = new GoogleCredential.Builder()
                    .setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)
                    .setServiceAccountId(clientEmail)
                    .setServiceAccountScopes(Collections.singletonList(StorageScopes.DEVSTORAGE_FULL_CONTROL))
                    .setServiceAccountPrivateKeyFromPemFile(privateKeyPemFile)
                    .setServiceAccountPrivateKeyId(privateKeyId)
                    .build();

        } catch (Exception e) {
            logger().error("Error on GCS Store initialization. Service won't work: " + e);
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

    @Override
    public HttpResponse getDocument(String documentUrl) throws GeneralSecurityException, IOException {
        HttpResponse response;
        HttpRequest request = buildHttpGetRequest(new GenericUrl(documentUrl));
        response = request.execute();
        return response;
    }

    private HttpRequest buildHttpGetRequest(GenericUrl url) throws IOException, GeneralSecurityException {
        HttpRequest request = requestFactory.buildGetRequest(url);
        return request;
    }

    private GenericUrl generateURLForDocument(String urlSuffix) {
        return new GenericUrl(sConfig.getEndpoint() + sConfig.getBucket() + "/"+urlSuffix);
    }

    private File createKeyPemFile(String privateKeyPem) throws IOException {
        File privateKeyPemFile = File.createTempFile("privateKeyPem", "txt");
        FileWriter fileWriter = new FileWriter(privateKeyPemFile);
        fileWriter.write(privateKeyPem);
        fileWriter.flush();
        fileWriter.close();
        return privateKeyPemFile;
    }
}
