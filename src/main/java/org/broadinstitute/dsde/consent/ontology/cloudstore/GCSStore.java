package org.broadinstitute.dsde.consent.ontology.cloudstore;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.broadinstitute.dsde.consent.ontology.OntologyLogger;
import org.broadinstitute.dsde.consent.ontology.configurations.StoreConfiguration;

public class GCSStore implements CloudStore, OntologyLogger {

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
      logException("Error on GCS Store initialization. Service won't work", e);
      throw new RuntimeException(e);
    }
    return credential;
  }


  @Override
  public HttpResponse getStorageDocument(String documentSuffix) throws IOException {
    HttpResponse response;
    HttpRequest request = buildHttpGetRequest(generateURLForDocument(documentSuffix));
    response = request.execute();
    return response;
  }

  private HttpRequest buildHttpGetRequest(GenericUrl url) throws IOException {
    return requestFactory.buildGetRequest(url);
  }

  private GenericUrl generateURLForDocument(String urlSuffix) {
    return new GenericUrl(sConfig.getEndpoint() + sConfig.getBucket() + "/" + urlSuffix);
  }

  public Bucket getBucketMetadata() throws IOException, GeneralSecurityException {
    Storage client = StorageFactory.getService(sConfig.getPassword());

    // com.google.api.services.storage.Storage.Buckets.get()
    return client.buckets().get(sConfig.getBucket()).execute();
  }

}
