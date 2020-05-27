package org.broadinstitute.dsde.consent.ontology.service;

import com.codahale.metrics.health.HealthCheck;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.eclipse.jetty.http.HttpMethod;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.nio.charset.Charset;

public class ElasticSearchHealthCheck extends HealthCheck implements Managed {

    private final Logger log = Utils.getLogger(this.getClass());
    private static final String GET = HttpMethod.GET.asString();
    private ElasticSearchConfiguration configuration;
    private RestClient client;
    private ElasticSearchSupport elasticSearchSupport;
    public static String NAME = "elastic-search";

    @Override
    public void start() { }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    @Inject
    public ElasticSearchHealthCheck(ElasticSearchConfiguration configuration) {
        this.configuration = configuration;
        this.elasticSearchSupport = new ElasticSearchSupport();
        this.client = elasticSearchSupport.createRestClient(this.configuration);
    }

    @Override
    protected Result check() {
        try {
            Request request = new Request(GET, elasticSearchSupport.getClusterHealthPath(configuration.getIndex()));
            Response response = elasticSearchSupport.retryRequest(client, request);
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error("Invalid health check request: " + response.getStatusLine().getReasonPhrase());
                throw new InternalServerErrorException(response.getStatusLine().getReasonPhrase());
            }
            String stringResponse = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            JsonObject jsonResponse = JsonParser.parseString(stringResponse).getAsJsonObject();
            boolean timeout = jsonResponse.get("timed_out").getAsBoolean();
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
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerErrorException();
        }
        return Result.healthy("ClusterHealth is GREEN");
    }

}
