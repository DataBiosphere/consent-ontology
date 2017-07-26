package org.broadinstitute.dsde.consent.ontology.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchAutocompleteAPI implements AutocompleteAPI {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPI.class);
    private static final String FIELD_ONTOLOGY_TYPE = "ontology";
    private static final String FIELD_USABLE = "usable";
    private final ElasticSearchConfiguration configuration;
    private JsonParser parser = new JsonParser();
    private Gson gson = new GsonBuilder().create();

    public ElasticSearchAutocompleteAPI(ElasticSearchConfiguration configuration) {
        this.configuration = configuration;
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
    private List<TermResource> executeSearch(String query, int limit, Boolean thinFilter) {
        List<TermResource> termList = new ArrayList<>();
        ElasticSearchSupport.validateIndexExists(configuration);
        try {
            try (RestClient client = ElasticSearchSupport.getRestClient(configuration)) {
                Map<String, String> params = new HashMap<>();
                params.put("size", String.valueOf(limit));
                Response esResponse = client.performRequest(
                    "GET",
                    ElasticSearchSupport.getSearchPath(configuration.getIndex()),
                    params,
                    new NStringEntity(query, ContentType.APPLICATION_JSON),
                    ElasticSearchSupport.jsonHeader);
                if (esResponse.getStatusLine().getStatusCode() != 200) {
                    logger.error("Invalid search request: " + esResponse.getStatusLine().getReasonPhrase());
                    throw new InternalServerErrorException();
                }
                String stringResponse = IOUtils.toString(esResponse.getEntity().getContent());
                JsonObject jsonResponse = parser.parse(stringResponse).getAsJsonObject();
                JsonArray hits = jsonResponse.getAsJsonObject("hits").getAsJsonArray("hits");
                for (JsonElement hit : hits) {
                    JsonElement data = hit.getAsJsonObject().getAsJsonObject("_source");
                    TermResource resource = gson.fromJson(data, TermResource.class);
                    if (thinFilter) {
                        resource.setOntology(null);
                        resource.setUsable(null);
                        resource.setParents(null);
                    }
                    termList.add(resource);
                }
            }
        } catch (IOException e) {
            logger.error("Unable to parse query response for " + query + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        return termList;
    }

    @Override
    public List<TermResource> lookup(String query, int limit) {
        return lookup(new String[0], query, limit);
    }

    @Override
    public List<TermResource> lookup(String[] tags, String query, int limit) {
        Multimap<String, String> filters = HashMultimap.create();
        filters.put(FIELD_USABLE, "true");
        for (String tag : tags) {
            filters.put(FIELD_ONTOLOGY_TYPE, tag);
        }
        return executeSearch(ElasticSearchSupport.buildFilterQuery(query, filters), limit, true);
    }

    @Override
    public List<TermResource> lookupById(String query) throws IOException {
        List<TermResource> terms = executeSearch(ElasticSearchSupport.buildIdQuery(query), 1, false);

        Collection<String> parentIds = terms.stream().
            flatMap(p -> Optional.ofNullable(p.getParents()).orElse(new ArrayList<>()).stream()).
            map(TermParent::getId).
            collect(Collectors.toList());

        Collection<TermResource> parentTerms = parentIds.stream().
            flatMap(t -> executeSearch(ElasticSearchSupport.buildIdQuery(t), 1, true).stream()).
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
