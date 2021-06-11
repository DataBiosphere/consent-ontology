package org.broadinstitute.dsde.consent.ontology.service;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

class ElasticSearchSupport {

    private static final Gson gson = new GsonBuilder().create();

    RestClient createRestClient(ElasticSearchConfiguration configuration) {
        HttpHost[] hosts = configuration
                .getServers()
                .stream()
                .map(server -> new HttpHost(server, configuration.getPort(), "http"))
                .collect(Collectors.toList())
                .toArray(new HttpHost[configuration.getServers().size()]);
        return RestClient
                .builder(hosts)
                .setDefaultHeaders(new Header[]{new BasicHeader("Content-Type", "application/json")})
                .build();
    }

    String getIndexPath(String index) {
        return "/" + index;
    }

    String getTermPath(String index) {
        return getIndexPath(index) + "/ontology_term";
    }

    String getSearchPath(String index) {
        return getTermPath(index) + "/_search";
    }

    String getClusterHealthPath() {
        return "/_cluster/health";
    }

    /**
     * Builds an elastic search json query object in the form of:
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
    String buildFilterQuery(String term, Collection<String> tags) {
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
    final String[] searchFields = {"id^3", "label^2", "synonyms", "definition"};

    Response retryRequest(final RestClient client, final Request request) {
        Callable<Response> callable = () -> client.performRequest(request);
        Retryer<Response> retryer = RetryerBuilder.<Response>newBuilder()
                .retryIfException()
                .withWaitStrategy(WaitStrategies.exponentialWait(10, 5, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();
        try {
            return retryer.call(callable);
        } catch (RetryException e) {
            if (e.getCause() instanceof ResponseException) {
                ResponseException re = (ResponseException) e.getCause();
                int status = re.getResponse().getStatusLine().getStatusCode();
                if (status == BAD_REQUEST.getStatusCode()) {
                    throw new BadRequestException();
                } else if (status == NOT_FOUND.getStatusCode()) {
                    throw new NotFoundException("Term not found");
                } else {
                    throw new InternalServerErrorException(e);
                }
            } else {
                throw new InternalServerErrorException(e);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }

    String getEncodedEndpoint(String query, String index) {
        try {
            return getTermPath(index) + "/" + java.net.URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BadRequestException(e);
        }
    }

}
