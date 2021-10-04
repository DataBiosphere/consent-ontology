package org.broadinstitute.dsde.consent.ontology.resources;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class StatusResourceTest {

    private Result deadlocks = Result.healthy();
    private Result elasticSearch = Result.healthy("ClusterHealth is GREEN");
    private Result gcs = Result.healthy();

    @Mock
    private HealthCheckRegistry healthChecks;

    private StatusResource initStatusResource(SortedMap<String, Result> checks) {
        openMocks(this);
        when(healthChecks.runHealthChecks()).thenReturn(checks);
        return new StatusResource(healthChecks);
    }

    @Test
    public void testHealthy() {
        SortedMap<String, Result> checks = new TreeMap<>();
        checks.put("deadlocks", deadlocks);
        checks.put("elastic-search", elasticSearch);
        checks.put("google-cloud-storage", gcs);
        StatusResource statusResource = initStatusResource(checks);

        Response response = statusResource.getStatus();
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testUnhealthyDeadlocks() {
        SortedMap<String, Result> checks = new TreeMap<>();
        checks.put("deadlocks", deadlocks);
        checks.put("elastic-search", Result.unhealthy("ClusterHealth is RED"));
        checks.put("google-cloud-storage", gcs);
        StatusResource statusResource = initStatusResource(checks);

        Response response = statusResource.getStatus();
        // We still expect a 200 response, but expect a warning log
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testUnhealthyElasticSearch() {
        SortedMap<String, Result> checks = new TreeMap<>();
        checks.put("deadlocks", Result.unhealthy(new Exception("Unhealthy Deadlocks")));
        checks.put("elastic-search", elasticSearch);
        checks.put("google-cloud-storage", gcs);
        StatusResource statusResource = initStatusResource(checks);

        Response response = statusResource.getStatus();
        // We still expect a 200 response, but expect a warning log
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    public void testUnhealthyGCS() {
        SortedMap<String, Result> checks = new TreeMap<>();
        checks.put("deadlocks", deadlocks);
        checks.put("elastic-search", elasticSearch);
        checks.put("google-cloud-storage", Result.unhealthy("GCS Unreachable"));
        StatusResource statusResource = initStatusResource(checks);

        Response response = statusResource.getStatus();
        // We still expect a 200 response, but expect a warning log
        Assert.assertEquals(200, response.getStatus());
    }

}
