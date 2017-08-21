package org.broadinstitute.dsde.consent.ontology.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ElasticSearchSupportTest {

    private ElasticSearchConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new ElasticSearchConfiguration();
        configuration.setIndex("local-ontology");
        configuration.setServers(Collections.singletonList("localhost"));
    }

    @Test
    public void testGetIndexPath() {
        String path = ElasticSearchSupport.getIndexPath(configuration);
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains(configuration.getIndex()));
    }

    @Test
    public void testGetClusterHealthPath() {
        String path = ElasticSearchSupport.getClusterHealthPath(configuration);
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains(configuration.getIndex()));
        Assert.assertTrue(path.contains("health"));
    }

    @Test
    public void testTermIdPath() throws Exception {
        String path = ElasticSearchSupport.getTermIdPath(configuration, "DOID_162");
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains("DOID_162"));
    }

    @Test
    public void testBaseIndexUrl() throws Exception {
        String path = ElasticSearchSupport.baseIndexUrl(configuration);
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains(configuration.getIndex()));
    }

    @Test
    public void testBaseServerUrl() throws Exception {
        String path = ElasticSearchSupport.baseServerUrl(configuration);
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains(configuration.getServers().get(0)));
    }

    @Test
    public void testBuildIdQuery() {
        String termId = "term_id";
        String query = ElasticSearchSupport.buildIdQuery(termId);
        JsonParser parser = new JsonParser();
        JsonObject jsonQuery = parser.parse(query).getAsJsonObject();
        Assert.assertTrue(jsonQuery.has("query"));
        Assert.assertTrue(jsonQuery.getAsJsonObject("query").has("match"));
        Assert.assertTrue(jsonQuery.getAsJsonObject("query").getAsJsonObject("match").has("id"));
        Assert.assertTrue(jsonQuery.getAsJsonObject("query").getAsJsonObject("match").get("id").getAsString().equals(termId));
    }

    @Test
    public void testBuildFilterQuery() {
        String termId = "term_id";
        List<String> filters = Arrays.asList("Disease", "Organization");
        String query = ElasticSearchSupport.buildFilterQuery(termId, filters);

        JsonParser parser = new JsonParser();

        JsonObject jsonQuery = parser.parse(query).getAsJsonObject();
        Assert.assertTrue(jsonQuery.has("query"));

        JsonObject joQuery = jsonQuery.getAsJsonObject("query");
        Assert.assertTrue(joQuery.has("bool"));

        JsonObject joBool = joQuery.getAsJsonObject("bool");
        Assert.assertTrue(joBool.has("must"));
        Assert.assertTrue(joBool.has("filter"));


        JsonObject joMust = joBool.getAsJsonObject("must");
        Assert.assertTrue(joMust.has("multi_match"));

        JsonObject joMultiMatch = joMust.getAsJsonObject("multi_match");
        Assert.assertTrue(joMultiMatch.has("query"));
        Assert.assertTrue(joMultiMatch.get("query").getAsString().equals(termId));
        Assert.assertTrue(joMultiMatch.has("type"));
        Assert.assertTrue(joMultiMatch.has("fields"));
        Assert.assertTrue(joMultiMatch.getAsJsonArray("fields").size() == ElasticSearchSupport.searchFields.length);

        JsonArray joFilter = joBool.getAsJsonArray("filter");
        Assert.assertTrue(joFilter.size() == 2);

    }

}
