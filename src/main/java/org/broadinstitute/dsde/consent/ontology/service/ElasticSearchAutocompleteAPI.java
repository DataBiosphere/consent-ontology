package org.broadinstitute.dsde.consent.ontology.service;

import com.google.gson.*;
import io.dropwizard.lifecycle.Managed;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchAutocompleteAPI implements AutocompleteAPI, Managed {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPI.class);
    private final ElasticSearchConfiguration configuration;
    private JsonParser parser = new JsonParser();
    private Gson gson = new GsonBuilder().create();
    private Client client;

    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
    }

    public ElasticSearchAutocompleteAPI(ElasticSearchConfiguration configuration, Client client) {
        this.configuration = configuration;
        this.client = client;
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
        try {
            String stringResponse;
            try {
                stringResponse = client.
                    target(ElasticSearchSupport.getSearchPath(configuration)).
                    queryParam("size", String.valueOf(limit)).
                    request(MediaType.APPLICATION_JSON).
                    post(Entity.entity(query, MediaType.APPLICATION_JSON)).
                    readEntity(String.class);
            } catch (Exception e) {
                logger.error("Invalid query request: " + e.getMessage());
                throw new InternalServerErrorException(e.getMessage());
            }
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
        } catch (Exception e) {
            logger.error("Unable to parse query response for " + query + "\n" + e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return termList;
    }

    @Override
    public List<TermResource> lookup(String query, int limit) {
        return lookup(Collections.emptyList(), query, limit);
    }

    @Override
    public List<TermResource> lookup(Collection<String> tags, String query, int limit) {
        return executeSearch(ElasticSearchSupport.buildFilterQuery(query, tags), limit, true);
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
