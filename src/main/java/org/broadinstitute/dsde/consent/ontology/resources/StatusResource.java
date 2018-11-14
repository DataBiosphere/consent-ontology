package org.broadinstitute.dsde.consent.ontology.resources;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("status")
public class StatusResource {

    private Logger logger() {
        return Logger.getLogger(StatusResource.class);
    }

    private HealthCheckRegistry healthChecks;

    public StatusResource(HealthCheckRegistry healthChecks) {
        this.healthChecks = healthChecks;
    }

    @GET
    @Produces("application/json")
    public Response getStatus() {
        Map<String, HealthCheck.Result> results = healthChecks.runHealthChecks();
        // Ontology can work in a degraded status so log errors at the warning level.
        results.entrySet().
                stream().
                filter(e -> !e.getValue().isHealthy()).
                forEach(e -> logger().warn("Error in service " + e.getKey() + ": " + formatResultError(e.getValue())));
        return Response.ok(results).build();
    }

    private String formatResultError(HealthCheck.Result result) {
        if (result.getMessage() != null) {
            return result.getMessage();
        } else if (result.getError() != null) {
            return result.getError().toString();
        }
        return "Healthcheck Result Error";
    }

}
