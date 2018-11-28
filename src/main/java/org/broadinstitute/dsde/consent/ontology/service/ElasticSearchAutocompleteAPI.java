package org.broadinstitute.dsde.consent.ontology.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.eclipse.jetty.http.HttpMethod;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElasticSearchAutocompleteAPI implements AutocompleteAPI, Managed {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPI.class);
    private static final String GET = HttpMethod.GET.asString();
    private final ElasticSearchConfiguration configuration;
    private JsonParser parser = new JsonParser();
    private Gson gson = new GsonBuilder().create();
    private RestClient client;
    private ElasticSearchSupport elasticSearchSupport;

    @Override
    public void start() { }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public ElasticSearchAutocompleteAPI(ElasticSearchConfiguration configuration) {
        this.configuration = configuration;
        setElasticSearchSupport(new ElasticSearchSupport());
        this.client = elasticSearchSupport.createRestClient(configuration);
    }

    @VisibleForTesting
    void setElasticSearchSupport(ElasticSearchSupport elasticSearchSupport) {
        this.elasticSearchSupport = elasticSearchSupport;
    }

    /**
     * Basic search execution method that queries ES and returns results.
     *
     * @param query      Query string in the form of an ES json query object
     * @param limit      How many to limit the results to
     * @param thinFilter When true, we provide the minimal amount of information to keep the API responses thin. When
     *                   false, we provide the fully populated object.
     * @return List of TermResources that match the query
     */
    @SuppressWarnings("SameParameterValue")
    private List<TermResource> executeSearch(String query, int limit, Boolean thinFilter) {
        List<TermResource> termList = new ArrayList<>();
        String endpoint = elasticSearchSupport.getSearchPath(configuration.getIndex());
        Request request = new Request(GET, endpoint);
        request.addParameter("size", String.valueOf(limit));
        request.setEntity(new NStringEntity(query, ContentType.APPLICATION_JSON));
        JsonObject jsonResponse = parseResponseToJson(elasticSearchSupport.retryRequest(client, request));
        JsonObject hitsSummary = jsonResponse.getAsJsonObject("hits");
        if (hitsSummary != null) {
            JsonArray hits = hitsSummary.getAsJsonArray("hits");
            for (JsonElement hit : hits) {
                JsonElement data = hit.getAsJsonObject().getAsJsonObject("_source");
                TermResource resource = gson.fromJson(data, TermResource.class);
                termList.add(filterThin(resource, thinFilter));
            }
        } else {
            logger.error("Unable to parse 'hits' from: " + jsonResponse);
        }
        return termList;
    }

    /**
     * Retrieve a single document from ES by its exact id.
     *
     * @param query      id of the document to return
     * @param thinFilter When true, we provide the minimal amount of information to keep the API responses thin. When
     *                   false, we provide the fully populated object.
     * @return List of TermResources that match the query. Will have either zero or one result.
     */
    private List<TermResource> executeGet(String query, Boolean thinFilter) {
        List<TermResource> termList = new ArrayList<>();
        String endpoint = elasticSearchSupport.getEncodedEndpoint(query, configuration.getIndex());
        Request request = new Request(GET, endpoint);
        JsonObject jsonResponse = parseResponseToJson(elasticSearchSupport.retryRequest(client, request));
        JsonElement data = jsonResponse.getAsJsonObject("_source");
        TermResource resource = gson.fromJson(data, TermResource.class);
        termList.add(filterThin(resource, thinFilter));
        return termList;
    }

    private TermResource filterThin(TermResource term, boolean thinFilter) {
        if (thinFilter) {
            term.setOntology(null);
            term.setUsable(null);
            term.setParents(null);
        }
        return term;
    }

    private JsonObject parseResponseToJson(Response response) {
        if (response.getStatusLine().getStatusCode() != 200) {
            logger.error("Invalid search request: " + response.getStatusLine().getReasonPhrase());
            throw new InternalServerErrorException(response.getStatusLine().getReasonPhrase());
        }
        try {
            String stringResponse = IOUtils.toString(response.getEntity().getContent());
            return parser.parse(stringResponse).getAsJsonObject();
        } catch (Exception e) {
            logger.error("Unable to parse response: ", e);
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<TermResource> lookup(String query, int limit) {
        return lookup(Collections.emptyList(), query, limit);
    }

    @Override
    public List<TermResource> lookup(Collection<String> tags, String query, int limit) {
        return executeSearch(elasticSearchSupport.buildFilterQuery(query, tags), limit, true);
    }

    @Override
    public List<TermResource> lookupById(String query) {
        List<TermResource> terms = executeGet(query, false);

        Collection<String> parentIds = terms.stream().
            flatMap(p -> Optional.ofNullable(p.getParents()).orElse(new ArrayList<>()).stream()).
            map(TermParent::getId).
            collect(Collectors.toList());

        Collection<TermResource> parentTerms = parentIds.stream().
            flatMap(t -> executeGet(t,true).stream()).
            collect(Collectors.toList());

        // Populate each of the parent nodes with more complete information
        for (TermResource term : terms) {
            for (TermParent p : Optional.ofNullable(term.getParents()).orElse(new ArrayList<>())) {
                Optional<TermResource> parentTermResource = parentTerms.stream().
                    filter(x -> x.getId().equals(p.getId())).
                    findFirst();
                if (parentTermResource.isPresent()) {
                    p.setLabel(parentTermResource.get().getLabel());
                    p.setSynonyms(parentTermResource.get().getSynonyms());
                    p.setDefinition(parentTermResource.get().getDefinition());
                }
            }
        }

        return terms;
    }

}
