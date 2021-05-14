package org.broadinstitute.dsde.consent.ontology.service;

import com.codahale.metrics.health.HealthCheck;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import io.dropwizard.lifecycle.Managed;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.eclipse.jetty.http.HttpMethod;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

public class ElasticSearchHealthCheck extends HealthCheck implements Managed {

    private static final String GET = HttpMethod.GET.asString();
    private final ElasticSearchConfiguration configuration;
    private final RestClient client;
    private final ElasticSearchSupport elasticSearchSupport;
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
            Request request = new Request(GET, elasticSearchSupport.getClusterHealthPath());
            Response response = elasticSearchSupport.retryRequest(client, request);
            if (response.getStatusLine().getStatusCode() != 200) {
                return Result.unhealthy(response.getStatusLine().getReasonPhrase());
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
                return Result.healthy("ClusterHealth is YELLOW\n" + jsonResponse.toString());
            }
        } catch (Throwable t) {
            return Result.unhealthy(t.getCause().getMessage());
        }
        return Result.healthy("ClusterHealth is GREEN");
    }

}
