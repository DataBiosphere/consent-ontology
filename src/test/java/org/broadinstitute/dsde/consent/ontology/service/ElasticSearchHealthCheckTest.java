package org.broadinstitute.dsde.consent.ontology.service;

import com.codahale.metrics.health.HealthCheck;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ElasticSearchHealthCheckTest {

    private ElasticSearchHealthCheck elasticSearchHealthCheck;

    @Mock
    private Client client;

    @Mock
    private AdminClient adminClient;

    @Mock
    private ClusterAdminClient clusterAdminClient;

    @Mock
    private ClusterHealthRequestBuilder clusterHealthRequestBuilder;

    @Mock
    private ClusterHealthResponse clusterHealthResponse;

    private final String INDEX = "ontology";

    @Before
    public void setUpClass() {
        MockitoAnnotations.initMocks(this);
        elasticSearchHealthCheck = new ElasticSearchHealthCheck(client, INDEX);
        when(client.admin()).thenReturn(adminClient);
        when(adminClient.cluster()).thenReturn(clusterAdminClient);
        when(clusterAdminClient.prepareHealth(INDEX)).thenReturn(clusterHealthRequestBuilder);
        when(clusterHealthRequestBuilder.get()).thenReturn(clusterHealthResponse);
    }

    @Test
    public void testCheckTimeOut() throws Exception {
        when(clusterHealthResponse.isTimedOut()).thenReturn(true);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("HealthCheck timed out"));
    }

    @Test
    public void testCheckStatusRed() throws Exception {
        when(clusterHealthResponse.getStatus()).thenReturn(ClusterHealthStatus.RED);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("ClusterHealth is RED"));
    }

    @Test
    public void testCheckStatusYellow() throws Exception {
        when(clusterHealthResponse.getStatus()).thenReturn(ClusterHealthStatus.YELLOW);
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertFalse(result.isHealthy());
        assertTrue(result.getMessage().contains("ClusterHealth is YELLOW"));
    }

    @Test
    public void testCheckStatusOK() throws Exception {
        HealthCheck.Result result = elasticSearchHealthCheck.check();
        assertTrue(result.isHealthy());
    }
}
