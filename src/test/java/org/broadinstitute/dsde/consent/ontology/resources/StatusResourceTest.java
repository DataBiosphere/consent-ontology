package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.util.SortedMap;
import java.util.TreeMap;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatusResourceTest {

  private final Result deadlocks = Result.healthy();
  private final Result elasticSearch = Result.healthy("ClusterHealth is GREEN");
  private final Result gcs = Result.healthy();

  @Mock
  private HealthCheckRegistry healthChecks;

  private StatusResource initStatusResource(SortedMap<String, Result> checks) {
    when(healthChecks.runHealthChecks()).thenReturn(checks);
    return new StatusResource(healthChecks);
  }

  @Test
  void testHealthy() {
    SortedMap<String, Result> checks = new TreeMap<>();
    checks.put("deadlocks", deadlocks);
    checks.put("elastic-search", elasticSearch);
    checks.put("google-cloud-storage", gcs);
    StatusResource statusResource = initStatusResource(checks);

    Response response = statusResource.getStatus();
    assertEquals(200, response.getStatus());
  }

  @Test
  void testUnhealthyDeadlocks() {
    SortedMap<String, Result> checks = new TreeMap<>();
    checks.put("deadlocks", deadlocks);
    checks.put("elastic-search", Result.unhealthy("ClusterHealth is RED"));
    checks.put("google-cloud-storage", gcs);
    StatusResource statusResource = initStatusResource(checks);

    Response response = statusResource.getStatus();
    // We still expect a 200 response, but expect a warning log
    assertEquals(200, response.getStatus());
  }

  @Test
  void testUnhealthyElasticSearch() {
    SortedMap<String, Result> checks = new TreeMap<>();
    checks.put("deadlocks", Result.unhealthy(new Exception("Unhealthy Deadlocks")));
    checks.put("elastic-search", elasticSearch);
    checks.put("google-cloud-storage", gcs);
    StatusResource statusResource = initStatusResource(checks);

    Response response = statusResource.getStatus();
    // We still expect a 200 response, but expect a warning log
    assertEquals(200, response.getStatus());
  }

  @Test
  void testUnhealthyGCS() {
    SortedMap<String, Result> checks = new TreeMap<>();
    checks.put("deadlocks", deadlocks);
    checks.put("elastic-search", elasticSearch);
    checks.put("google-cloud-storage", Result.unhealthy("GCS Unreachable"));
    StatusResource statusResource = initStatusResource(checks);

    Response response = statusResource.getStatus();
    // We still expect a 200 response, but expect a warning log
    assertEquals(200, response.getStatus());
  }

}
