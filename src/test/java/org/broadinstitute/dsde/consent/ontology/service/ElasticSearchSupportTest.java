package org.broadinstitute.dsde.consent.ontology.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertNotNull(client);
    }

    @Test
    public void testGetIndexPath() {
        String path = elasticSearchSupport.getIndexPath(configuration.getIndex());
        Assertions.assertNotNull(path);
        Assertions.assertTrue(path.contains(configuration.getIndex()));
    }

    @Test
    public void testGetClusterHealthPath() {
        String path = elasticSearchSupport.getClusterHealthPath();
        Assertions.assertNotNull(path);
        Assertions.assertTrue(path.contains("health"));
    }

    @Test
    public void testBuildFilterQuery() {
        String termId = "term_id";
        List<String> filters = Arrays.asList("Disease", "Organization");
        String query = elasticSearchSupport.buildFilterQuery(termId, filters);

        JsonObject jsonQuery = JsonParser.parseString(query).getAsJsonObject();
        Assertions.assertTrue(jsonQuery.has("query"));

        JsonObject joQuery = jsonQuery.getAsJsonObject("query");
        Assertions.assertTrue(joQuery.has("bool"));

        JsonObject joBool = joQuery.getAsJsonObject("bool");
        Assertions.assertTrue(joBool.has("must"));
        Assertions.assertTrue(joBool.has("filter"));


        JsonObject joMust = joBool.getAsJsonObject("must");
        Assertions.assertTrue(joMust.has("multi_match"));

        JsonObject joMultiMatch = joMust.getAsJsonObject("multi_match");
        Assertions.assertTrue(joMultiMatch.has("query"));
        Assertions.assertEquals(joMultiMatch.get("query").getAsString(), termId);
        Assertions.assertTrue(joMultiMatch.has("type"));
        Assertions.assertTrue(joMultiMatch.has("fields"));
        Assertions.assertEquals(joMultiMatch.getAsJsonArray("fields").size(),
            elasticSearchSupport.searchFields.length);

        JsonArray joFilter = joBool.getAsJsonArray("filter");
        Assertions.assertEquals(2, joFilter.size());

    }

}
