package org.broadinstitute.dsde.consent.ontology.service;

import com.codahale.metrics.health.HealthCheck;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.dropwizard.lifecycle.Managed;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

public class ElasticSearchHealthCheck extends HealthCheck implements Managed {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ElasticSearchHealthCheck.class);
    private ElasticSearchConfiguration configuration;
    private JsonParser parser = new JsonParser();
    private Client client;


    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public ElasticSearchHealthCheck(ElasticSearchConfiguration config, Client client) {
        this.configuration = config;
        this.client = client;
    }

    @Override
    protected Result check() throws Exception {
        try {
            String stringResponse;
            try {
                stringResponse = client.
                    target(ElasticSearchSupport.getClusterHealthPath(configuration)).
                    request(MediaType.APPLICATION_JSON).
                    get().
                    readEntity(String.class);
            } catch (Exception e) {
                logger.error("Invalid health check request: " + e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
            JsonObject jsonResponse = parser.parse(stringResponse).getAsJsonObject();
            Boolean timeout = jsonResponse.get("timed_out").getAsBoolean();
            String status = jsonResponse.get("status").getAsString();
            if (timeout) {
                return Result.unhealthy("HealthCheck timed out");
            }
            if (status.equalsIgnoreCase("red")) {
                return Result.unhealthy("ClusterHealth is RED\n" + jsonResponse.toString());
            }
            if (status.equalsIgnoreCase("yellow")) {
                return Result.unhealthy("ClusterHealth is YELLOW\n" + jsonResponse.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerErrorException();
        }
        return Result.healthy("ClusterHealth is GREEN");
    }
}
