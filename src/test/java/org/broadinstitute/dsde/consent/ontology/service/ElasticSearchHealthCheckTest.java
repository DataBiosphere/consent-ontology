package org.broadinstitute.dsde.consent.ontology.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.codahale.metrics.health.HealthCheck;
import com.google.api.client.http.HttpStatusCodes;
import java.util.Collections;
import org.broadinstitute.dsde.consent.ontology.WithMockServer;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;

public class ElasticSearchHealthCheckTest implements WithMockServer {

  private ElasticSearchHealthCheck elasticSearchHealthCheck;
  private MockServerClient mockServerClient;
  private final MockServerContainer container = new MockServerContainer(IMAGE);

  @BeforeEach
  void setUpClass() {
    container.start();
    ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
    configuration.setIndex("test-ontology");
    configuration.setServers(Collections.singletonList("localhost"));
    configuration.setPort(container.getServerPort());
    elasticSearchHealthCheck = new ElasticSearchHealthCheck(configuration);
    mockServerClient = new MockServerClient(container.getHost(), container.getServerPort());
  }

  @AfterEach
  void shutDown() {
    stop(container);
  }

  private void mockRequest(String color, Boolean timedOut) {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).
            withBody("{\n" +
                "  \"cluster_name\": \"docker-cluster\",\n" +
                "  \"status\": \"" + color + "\",\n" +
                "  \"timed_out\": " + timedOut + ",\n" +
                "  \"number_of_nodes\": 1,\n" +
                "  \"number_of_data_nodes\": 1,\n" +
                "  \"active_primary_shards\": 2,\n" +
                "  \"active_shards\": 2,\n" +
                "  \"relocating_shards\": 0,\n" +
                "  \"initializing_shards\": 0,\n" +
                "  \"unassigned_shards\": 2,\n" +
                "  \"delayed_unassigned_shards\": 0,\n" +
                "  \"number_of_pending_tasks\": 0,\n" +
                "  \"number_of_in_flight_fetch\": 0,\n" +
                "  \"task_max_waiting_in_queue_millis\": 0,\n" +
                "  \"active_shards_percent_as_number\": 50\n" +
                "}"));
  }

  @Test
  public void testCheckTimeOut() {
    mockRequest("red", true);
    HealthCheck.Result result = elasticSearchHealthCheck.check();
    assertFalse(result.isHealthy());
    assertTrue(result.getMessage().contains("HealthCheck timed out"));
  }

  @Test
  public void testCheckStatusRed() {
    mockRequest("red", false);
    HealthCheck.Result result = elasticSearchHealthCheck.check();
    assertFalse(result.isHealthy());
    assertTrue(result.getMessage().contains("ClusterHealth is RED"));
  }

  @Test
  public void testCheckStatusYellow() {
    mockRequest("yellow", false);
    HealthCheck.Result result = elasticSearchHealthCheck.check();
    assertTrue(result.isHealthy());
    assertTrue(result.getMessage().contains("ClusterHealth is YELLOW"));
  }

  @Test
  public void testCheckStatusOK() {
    mockRequest("green", false);
    HealthCheck.Result result = elasticSearchHealthCheck.check();
    assertTrue(result.isHealthy());
  }

  @Test
  public void testCheckDroppedConnection() {
    mockServerClient.when(request()).error(error().withDropConnection(true));
    HealthCheck.Result result = elasticSearchHealthCheck.check();
    assertFalse(result.isHealthy());
  }

  @Test
  public void testErrorStatus() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_SERVER_ERROR));
    HealthCheck.Result result = elasticSearchHealthCheck.check();
    assertFalse(result.isHealthy());
  }

}
