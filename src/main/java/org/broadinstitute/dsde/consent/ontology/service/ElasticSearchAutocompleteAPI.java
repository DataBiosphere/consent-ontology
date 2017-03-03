package org.broadinstitute.dsde.consent.ontology.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ElasticSearchAutocompleteAPI implements AutocompleteAPI {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPI.class);
    private static final String FIELD_ID = "id";
    private static final String FIELD_ONTOLOGY_TYPE = "ontology";
    private static final String FIELD_LABEL = "label";
    private static final String FIELD_SYNONYM = "synonyms";
    private static final String FIELD_USABLE = "usable";
    private final Client client;
    private final String index;

    public ElasticSearchAutocompleteAPI(Client client, String index) {
        this.client = client;
        this.index = index;
    }

    private QueryBuilder buildQuery(String term) {
        return QueryBuilders.multiMatchQuery(term, String.format("%s^4", FIELD_ID), String.format("%s^2", FIELD_LABEL), FIELD_SYNONYM).type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX);
    }

    private QueryBuilder buildQueryById(String term) {
        return QueryBuilders.matchQuery(FIELD_ID, term);
    }

    /**
     * Basic search execution method that queries ES and returns results.
     *
     * @param qb QueryBuilder
     * @param limit How many to limit the results to
     * @param thinFilter When true, we provide the minimal amount of information to keep the API responses thin. When
     *                   false, we provide the fully populated object.
     * @return List of TermResources that match the query
     */
    private List<TermResource> executeSearch(QueryBuilder qb, int limit, Boolean thinFilter) {
        List<TermResource> termList = new ArrayList<>();
        SearchHits hits = client.prepareSearch(index).setQuery(qb).setSize(limit).execute().actionGet().getHits();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : hits.getHits()) {
            String jsonString = hit.getSourceAsString();
            try {
                TermResource resource = mapper.readValue(jsonString, TermResource.class);
                if (thinFilter) {
                    resource.setOntology(null);
                    resource.setUsable(null);
                    resource.setParents(null);
                }
                termList.add(resource);
            } catch (IOException e) {
                logger.error("Exception mapping value: '" + jsonString + "' to TermResource: " + e.getMessage());
            }
        }
        return termList;
    }

    @Override
    public List<TermResource> lookup(String query, int limit) {
        return lookup(new String[0], query, limit);
    }

    @Override
    public List<TermResource> lookup(String[] tags, String query, int limit) {
        FilterBuilder deprecationFilter = FilterBuilders.termFilter(FIELD_USABLE, true);
        FilterBuilder filter;
        if (tags.length > 0) {
            filter = FilterBuilders.andFilter(deprecationFilter, FilterBuilders.termsFilter(FIELD_ONTOLOGY_TYPE, tags));
        } else {
            filter = deprecationFilter;
        }
        QueryBuilder queryBuilder = buildQuery(query);
        return executeSearch(QueryBuilders.filteredQuery(queryBuilder, filter), limit, true);
    }

    @Override
    public List<TermResource> lookupById(String query) {
        List<TermResource> terms = executeSearch(buildQueryById(query), 1, false);

        Collection<String> parentIds = terms.stream().
            flatMap(p -> Optional.ofNullable(p.getParents()).orElse(new ArrayList<>()).stream()).
            map(TermParent::getId).
            collect(Collectors.toList());

        Collection<TermResource> parentTerms = parentIds.stream().
            flatMap(t -> executeSearch(buildQueryById(t), 1, true).stream()).
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
