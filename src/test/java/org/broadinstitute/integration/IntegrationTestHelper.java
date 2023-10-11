package org.broadinstitute.integration;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

public interface IntegrationTestHelper {

  public record SimpleResponse(int code, String entity) {

  }

  /**
   * Integration tests can pass in an alternative url to test against. By default, we'll test
   * against develop.
   *
   * @return Base URL string: `baseUrl`
   */
  default String getBaseUrl() {
    String baseUrl = System.getenv("baseUrl");
    return Optional.ofNullable(baseUrl)
        .filter(Predicate.not(String::isBlank))
        .orElse("https://consent-ontology.dsde-dev.broadinstitute.org/");
  }

  int poolSize = 5;

  long delay = 30;

  default SimpleResponse fetchGetResponse(String path) throws Exception {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      HttpGet request = new HttpGet(getBaseUrl() + path);
      final ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize);
      executor.schedule(request::cancel, delay, TimeUnit.SECONDS);
      return client.execute(request, httpResponse ->
          new SimpleResponse(
              httpResponse.getCode(),
              IOUtils.toString(httpResponse.getEntity().getContent(), Charset.defaultCharset())));
    }
  }

  default SimpleResponse fetchGetResponseWithQueryParam(String path, String term, String val) throws Exception {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      URI uri = new URIBuilder(getBaseUrl() + path).addParameter(term, val).build();
      HttpGet request = new HttpGet(uri);
      final ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize);
      executor.schedule(request::cancel, delay, TimeUnit.SECONDS);
      return client.execute(request, httpResponse ->
          new SimpleResponse(
              httpResponse.getCode(),
              IOUtils.toString(httpResponse.getEntity().getContent(), Charset.defaultCharset())));
    }
  }

  default SimpleResponse getPostResponse(String path, String body) throws Exception {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      URI uri = new URIBuilder(getBaseUrl() + path).build();
      HttpPost request = new HttpPost(uri);
      request.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON);
      request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
      final ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize);
      executor.schedule(request::cancel, delay, TimeUnit.SECONDS);
      return client.execute(request, httpResponse ->
          new SimpleResponse(
              httpResponse.getCode(),
              IOUtils.toString(httpResponse.getEntity().getContent(), Charset.defaultCharset())));
    }
  }

  default SimpleResponse getPostResponseWithQueryParam(String path, String body, String term, String val) throws Exception {
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      URI uri = new URIBuilder(getBaseUrl() + path).addParameter(term, val).build();
      HttpPost request = new HttpPost(uri);
      request.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON);
      request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
      final ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize);
      executor.schedule(request::cancel, delay, TimeUnit.SECONDS);
      return client.execute(request, httpResponse ->
          new SimpleResponse(
              httpResponse.getCode(),
              IOUtils.toString(httpResponse.getEntity().getContent(), Charset.defaultCharset())));
    }
  }

}
