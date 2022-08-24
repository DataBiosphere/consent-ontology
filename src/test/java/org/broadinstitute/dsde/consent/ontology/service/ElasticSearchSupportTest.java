package org.broadinstitute.dsde.consent.ontology.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ElasticSearchSupportTest {

    private ElasticSearchConfiguration configuration;
    private ElasticSearchSupport elasticSearchSupport;

    @BeforeEach
    public void setUp() {
        configuration = new ElasticSearchConfiguration();
        configuration.setIndex("local-ontology");
        configuration.setServers(Collections.singletonList("localhost"));
        this.elasticSearchSupport = new ElasticSearchSupport();
    }

    @Test
    public void testGetRestClient() {
        RestClient client = elasticSearchSupport.createRestClient(configuration);
        assertNotNull(client);
    }

    @Test
    public void testGetIndexPath() {
        String path = elasticSearchSupport.getIndexPath(configuration.getIndex());
        assertNotNull(path);
        assertTrue(path.contains(configuration.getIndex()));
    }

    @Test
    public void testGetClusterHealthPath() {
        String path = elasticSearchSupport.getClusterHealthPath();
        assertNotNull(path);
        assertTrue(path.contains("health"));
    }

    @Test
    public void testBuildFilterQuery() {
        String termId = "term_id";
        List<String> filters = Arrays.asList("Disease", "Organization");
        String query = elasticSearchSupport.buildFilterQuery(termId, filters);

        JsonObject jsonQuery = JsonParser.parseString(query).getAsJsonObject();
        assertTrue(jsonQuery.has("query"));

        JsonObject joQuery = jsonQuery.getAsJsonObject("query");
        assertTrue(joQuery.has("bool"));

        JsonObject joBool = joQuery.getAsJsonObject("bool");
        assertTrue(joBool.has("must"));
        assertTrue(joBool.has("filter"));


        JsonObject joMust = joBool.getAsJsonObject("must");
        assertTrue(joMust.has("multi_match"));

        JsonObject joMultiMatch = joMust.getAsJsonObject("multi_match");
        assertTrue(joMultiMatch.has("query"));
        assertEquals(joMultiMatch.get("query").getAsString(), termId);
        assertTrue(joMultiMatch.has("type"));
        assertTrue(joMultiMatch.has("fields"));
        assertEquals(joMultiMatch.getAsJsonArray("fields").size(),
            elasticSearchSupport.searchFields.length);

        JsonArray joFilter = joBool.getAsJsonArray("filter");
        assertEquals(2, joFilter.size());

    }

}
