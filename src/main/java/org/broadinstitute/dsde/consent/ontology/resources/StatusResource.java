package org.broadinstitute.dsde.consent.ontology.resources;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.inject.Inject;
import java.util.LinkedHashMap;
import org.broadinstitute.dsde.consent.ontology.OntologyApp;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSHealthCheck;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchHealthCheck;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("status")
public class StatusResource {

    public static final String OK = "ok";
    public static final String DEGRADED = "degraded";
    public static final String SYSTEMS = "systems";

    private final Logger log = Utils.getLogger(this.getClass());

    private final HealthCheckRegistry healthChecks;

    @Inject
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
                forEach(e -> log.warn("Error in service " + e.getKey() + ": " + formatResultError(e.getValue())));
        return Response.ok(formatResults(results)).build();
    }

    private Map<String, Object> formatResults(Map<String, HealthCheck.Result> results) {
        // Order is important. Put Ok and Degraded states at the beginning of the map and
        // add all other entries after that.
        Map<String, Object> formattedResults = new LinkedHashMap<>();
        formattedResults.put(OK, true);
        HealthCheck.Result gcs = results.getOrDefault(GCSHealthCheck.NAME, HealthCheck.Result.unhealthy("Unable to access GCS"));
        HealthCheck.Result elasticSearch = results.getOrDefault(ElasticSearchHealthCheck.NAME, HealthCheck.Result.unhealthy("Unable to access ElasticSearch"));
        boolean degraded = (!gcs.isHealthy() || !elasticSearch.isHealthy());
        formattedResults.put(DEGRADED, degraded);
        formattedResults.put(SYSTEMS, results);
        return formattedResults;
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
