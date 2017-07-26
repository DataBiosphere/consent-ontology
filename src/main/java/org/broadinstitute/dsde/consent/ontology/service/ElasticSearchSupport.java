package org.broadinstitute.dsde.consent.ontology.service;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchSupport {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchSupport.class);

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
     * { "query" : { "match" : { "id": "DOID_162" } } }
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
     {
         "query": {
             "bool": {
                 "must": {
                     "multi_match" : {
                     "query": "child",
                     "type": "phrase_prefix",
                     "fields": [ "id^3", "label^2", "synonyms", "definition" ]
                     }
                 },
                 "filter": [
                     { "term": { "usable": "true" } },
                     { "term": { "ontology": "organization" } }
                 ]
             }
         }
     }
     * @param term Query term string
     * @param filters List of filter terms that should be applied.
     * @return Json formatted string suitable for using as an Elastic Search query object.
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
        // Boolean entry order is important (so we use a LinkedHashMap). Writing the filter first will slow down ES query times.
        Map<String, Object> bool = new LinkedHashMap<>();
        Map<String, Object> must = new HashMap<>();
        Map<String, Object> multiMatch = new LinkedHashMap<>(); // Preserve insert order
        multiMatch.put("query", term);
        multiMatch.put("type", "phrase_prefix");
        multiMatch.put("fields", searchFields);
        must.put("multi_match", multiMatch);
        bool.put("must", must);
        bool.put("filter", termList);
        query.put("bool", bool);
        jsonQuery.put("query", query);
        return gson.toJson(jsonQuery);
    }

    /*
     * Preferred search fields are boosted by default, e.g., '^3' triples a field's weight.
     */
    public static final String[] searchFields = {"id^3", "label^2", "synonyms", "definition"};

    /**
     * Check to see if the index exists
     *
     * @throws InternalServerErrorException The exception
     */
    public static void validateIndexExists(ElasticSearchConfiguration configuration) throws InternalServerErrorException {
        try(RestClient client = getRestClient(configuration)) {
            Response esResponse = client.performRequest("GET", getIndexPath(configuration.getIndex()), jsonHeader);
            if (esResponse.getStatusLine().getStatusCode() != 200) {
                logger.error("Invalid index request: " + esResponse.getStatusLine().getReasonPhrase());
                throw new InternalServerErrorException(esResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }


}
