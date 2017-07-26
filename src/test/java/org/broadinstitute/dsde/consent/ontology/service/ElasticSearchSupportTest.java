package org.broadinstitute.dsde.consent.ontology.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.RestClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;
import org.openjena.atlas.lib.MultiMap;

import java.util.Collections;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ElasticSearchSupportTest {

    private ElasticSearchConfiguration configuration;
    private ClientAndServer server;

    @Before
    public void setUp() throws Exception {
        configuration = new ElasticSearchConfiguration();
        configuration.setIndex("local-ontology");
        configuration.setServers(Collections.singletonList("localhost"));
        server = startClientAndServer(9200);
    }

    @After
    public void shutDown() throws Exception {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    private void mockResponse(HttpResponse response) {
        server.reset();
        server.when(request()).respond(response);
    }

    @Test
    public void testGetRestClient() {
        RestClient client = ElasticSearchSupport.getRestClient(configuration);
        Assert.assertNotNull(client);
    }

    @Test
    public void testGetIndexPath() {
        String path = ElasticSearchSupport.getIndexPath(configuration.getIndex());
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains(configuration.getIndex()));
    }

    @Test
    public void testGetClusterHealthPath() {
        String path = ElasticSearchSupport.getClusterHealthPath(configuration.getIndex());
        Assert.assertNotNull(path);
        Assert.assertTrue(path.contains(configuration.getIndex()));
        Assert.assertTrue(path.contains("health"));
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
        Multimap<String, String> filters = HashMultimap.create();
        filters.put("usable", "true");
        filters.put("ontology", "Disease");
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

    @Test
    public void testValidateIndexExists() {
        mockResponse(response().withStatusCode(200).withBody(indexJson));
        try {
            ElasticSearchSupport.validateIndexExists(configuration);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    private static String indexJson = "{\n" +
        "  \"local-ontology\": {\n" +
        "    \"aliases\": {},\n" +
        "    \"mappings\": {\n" +
        "      \"ontology_term\": {\n" +
        "        \"properties\": {\n" +
        "          \"definition\": {\n" +
        "            \"type\": \"text\",\n" +
        "            \"fields\": {\n" +
        "              \"keyword\": {\n" +
        "                \"type\": \"keyword\",\n" +
        "                \"ignore_above\": 256\n" +
        "              }\n" +
        "            }\n" +
        "          },\n" +
        "          \"id\": {\n" +
        "            \"type\": \"text\",\n" +
        "            \"fields\": {\n" +
        "              \"keyword\": {\n" +
        "                \"type\": \"keyword\",\n" +
        "                \"ignore_above\": 256\n" +
        "              }\n" +
        "            }\n" +
        "          },\n" +
        "          \"label\": {\n" +
        "            \"type\": \"text\",\n" +
        "            \"fields\": {\n" +
        "              \"keyword\": {\n" +
        "                \"type\": \"keyword\",\n" +
        "                \"ignore_above\": 256\n" +
        "              }\n" +
        "            }\n" +
        "          },\n" +
        "          \"ontology\": {\n" +
        "            \"type\": \"text\",\n" +
        "            \"fields\": {\n" +
        "              \"keyword\": {\n" +
        "                \"type\": \"keyword\",\n" +
        "                \"ignore_above\": 256\n" +
        "              }\n" +
        "            }\n" +
        "          },\n" +
        "          \"parents\": {\n" +
        "            \"properties\": {\n" +
        "              \"id\": {\n" +
        "                \"type\": \"text\",\n" +
        "                \"fields\": {\n" +
        "                  \"keyword\": {\n" +
        "                    \"type\": \"keyword\",\n" +
        "                    \"ignore_above\": 256\n" +
        "                  }\n" +
        "                }\n" +
        "              },\n" +
        "              \"order\": {\n" +
        "                \"type\": \"long\"\n" +
        "              }\n" +
        "            }\n" +
        "          },\n" +
        "          \"synonyms\": {\n" +
        "            \"type\": \"text\",\n" +
        "            \"fields\": {\n" +
        "              \"keyword\": {\n" +
        "                \"type\": \"keyword\",\n" +
        "                \"ignore_above\": 256\n" +
        "              }\n" +
        "            }\n" +
        "          },\n" +
        "          \"usable\": {\n" +
        "            \"type\": \"boolean\"\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    },\n" +
        "    \"settings\": {\n" +
        "      \"index\": {\n" +
        "        \"creation_date\": \"1500914949437\",\n" +
        "        \"number_of_shards\": \"5\",\n" +
        "        \"number_of_replicas\": \"2\",\n" +
        "        \"uuid\": \"xF4Zsd1ZS66yYsKgFgMDew\",\n" +
        "        \"version\": {\n" +
        "          \"created\": \"5050099\"\n" +
        "        },\n" +
        "        \"provided_name\": \"local-ontology\"\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}";

}
