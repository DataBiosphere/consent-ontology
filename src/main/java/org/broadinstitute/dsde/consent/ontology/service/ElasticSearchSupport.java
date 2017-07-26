package org.broadinstitute.dsde.consent.ontology.service;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchSupport {

    public static RestClient getRestClient(ElasticSearchConfiguration configuration) {
        HttpHost[] hosts = configuration.
            getServers().
            stream().
            map(server -> new HttpHost(server, 9200, "http")).
            collect(Collectors.toList()).toArray(new HttpHost[configuration.getServers().size()]);
        return RestClient.builder(hosts).build();
    }

    public static String getIndexPath(String index) {
        return "/" + index;
    }

    public static String getSearchPath(String index) {
        return "/" + index + "/ontology_term/_search";
    }

    public static String getClusterHealthPath(String index) {
        return "/_cluster/health/" + index;
    }

    public static Header jsonHeader = new BasicHeader("Content-Type", "application/json");

    private static Gson gson = new GsonBuilder().create();

    /**
     * Builds a json object in the form of:
     *
     "{" +
     "  \"query\": {" +
     "    \"match\" : {" +
     "      \"id\": \"" + term + "\" " +
     "    }" +
     "  }" +
     "}";
     *
     * @param termId Query term string
     * @return Json formatted string suitable for using as an Elastic Search query object.
     */
    public static String buildIdQuery(String termId) {
        Map<String, Object> jsonQuery = new HashMap<>();
        Map<String, Object> query = new HashMap<>();
        Map<String, String> match = new HashMap<>();
        match.put("id", termId);
        query.put("match", match);
        jsonQuery.put("query", query);
        return gson.toJson(jsonQuery);
    }

    /**
     * Builds a json object in the form of:
     *
     "{" +
     "  \"query\": {" +
     "    \"bool\": {" +
     "      \"must\": {" +
     "        \"multi_match\" : {" +
     "          \"query\": \"" + term + "\", " +
     "          \"type\": \"phrase_prefix\", " +
     "          \"fields\": [ \"id^3\", \"label^2\", \"synonyms\", \"definition\" ] " +
     "        }" +
     "      }," +
     "      \"filter\": [{ \"term\": { \"" + filter.entry.getKey() + "\": \"" + filter.entry.getValue() + "\" } }]" +
     "    }" +
     "  }" +
     "}";
     * @param term
     * @param filters
     * @return
     */
    public static String buildFilterQuery(String term, Multimap<String, String> filters) {
        List<Map<String, Object>> termList = new ArrayList<>();
        for (Map.Entry<String, String> entry: filters.entries()) {
            Map<String, String> ft = new HashMap<>();
            ft.put(entry.getKey(), entry.getValue().toLowerCase());
            Map<String, Object> tl = new HashMap<>();
            tl.put("term", ft);
            termList.add(tl);
        }
        Map<String, Object> jsonQuery = new HashMap<>();
        Map<String, Object> query = new HashMap<>();
        Map<String, Object> bool = new LinkedHashMap<>();
        Map<String, Object> must = new HashMap<>();
        Map<String, Object> multiMatch = new HashMap<>();
        multiMatch.put("query", term);
        multiMatch.put("type", "phrase_prefix");
        multiMatch.put("fields", searchFields);
        must.put("multi_match", multiMatch);
        // Boolean entry order is important (so we use a LinkedHashMap). Writing the filter first will slow down ES query times.
        bool.put("must", must);
        bool.put("filter", termList);
        query.put("bool", bool);
        jsonQuery.put("query", query);
        return gson.toJson(jsonQuery);
    }

    public static final String[] searchFields = {"id^3", "label^2", "synonyms", "definition"};

}
