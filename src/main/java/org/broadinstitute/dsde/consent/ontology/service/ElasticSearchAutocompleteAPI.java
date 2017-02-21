package org.broadinstitute.dsde.consent.ontology.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.broadinstitute.dsde.consent.ontology.resources.TermResource;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchAutocompleteAPI implements AutocompleteAPI {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPI.class);
    private static final String FIELD_ID = "id";
    private static final String FIELD_ONTOLOGY_TYPE = "ontology";
    private static final String FIELD_LABEL = "label";
    private static final String FIELD_SYNONYM = "synonym";
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

    private List<TermResource> executeSearch(QueryBuilder qb, int limit) {
        List<TermResource> termList = new ArrayList<>();
        SearchHits hits = client.prepareSearch(index).setQuery(qb).setSize(limit).execute().actionGet().getHits();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : hits.getHits()) {
            String jsonString = hit.getSourceAsString();
            try {
                TermResource resource = mapper.readValue(jsonString, TermResource.class);
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
        return executeSearch(QueryBuilders.filteredQuery(queryBuilder, filter), limit);
    }

    @Override
    public List<TermResource> lookupById(String query) {
        return executeSearch(buildQueryById(query), 1);
    }
}
