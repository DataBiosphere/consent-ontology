package org.broadinstitute.dsde.consent.ontology.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ElasticSearchSupport {

    public static String getIndexPath(ElasticSearchConfiguration config) {
        return baseIndexUrl(config);
    }

    public static String getSearchPath(ElasticSearchConfiguration config) {
        return baseIndexUrl(config) + "/ontology_term/_search";
    }

    public static String getClusterHealthPath(ElasticSearchConfiguration config) {
        return baseServerUrl(config) + "_cluster/health/" + config.getIndex();
    }

    public static String getTermIdPath(ElasticSearchConfiguration config, String termId) throws UnsupportedEncodingException {
        return baseIndexUrl(config) + "/ontology_term/" + URLEncoder.encode(termId, "UTF-8");
    }

    public static String chooseRandomServer(ElasticSearchConfiguration config) {
        int next = ThreadLocalRandom.current().nextInt(config.getServers().size());
        return config.getServers().get(next);
    }

    public static String baseIndexUrl(ElasticSearchConfiguration config) {
        return baseServerUrl(config) + config.getIndex();
    }

    public static String baseServerUrl(ElasticSearchConfiguration config) {
        String server = ElasticSearchSupport.chooseRandomServer(config);
        return "http://" + server + ":9200/";
    }

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
                     { "terms": { "ontology": ["organization", "disease"] } }
                 ]
             }
         }
     }
     * @param term Query term string
     * @param tags List of ontology type filter tags that should be applied. Filter of `usable=true` is always applied
     * @return Json formatted string suitable for using as an Elastic Search query object.
     */
    public static String buildFilterQuery(String term, Collection<String> tags) {
        List<Map<String, Object>> filterList = new ArrayList<>();
        Map<String, Object> usable = new HashMap<>();
        usable.put("usable", "true");
        Map<String, Object> usableTerm = new HashMap<>();
        usableTerm.put("term", usable);
        filterList.add(usableTerm);
        if (!tags.isEmpty()) {
            Collection<String> lowerCasedTags = tags.
                stream().
                map(tag -> tag.toLowerCase().trim()).
                collect(Collectors.toList());
            Map<String, Object> tl = new HashMap<>();
            Map<String, Object> ontologyTypes = new HashMap<>();
            ontologyTypes.put("ontology", lowerCasedTags);
            tl.put("terms", ontologyTypes);
            filterList.add(tl);
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
        bool.put("filter", filterList);
        query.put("bool", bool);
        jsonQuery.put("query", query);
        return gson.toJson(jsonQuery);
    }

    /*
     * Preferred search fields are boosted by default, e.g., '^3' triples a field's weight.
     */
    public static final String[] searchFields = {"id^3", "label^2", "synonyms", "definition"};


}
