package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.resources.TermResource;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.StringText;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.internal.InternalSearchHit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchAutocompleteAPITest {

    private ElasticSearchAutocompleteAPI elasticSearchAutocompleteAPI;

    @Mock
    private Client client;

    @Mock
    private SearchRequestBuilder queryBuilder;

    @Mock
    private ListenableActionFuture listenableActionFuture;

    @Mock
    private SearchResponse searchResponse;

    @Mock
    private SearchHits hits;

    private final String INDEX = "ontology";

    private final String QUERY = "name=test";

    @Before
    public void setUpClass() {
        MockitoAnnotations.initMocks(this);
        elasticSearchAutocompleteAPI = new ElasticSearchAutocompleteAPI(client, INDEX);
        when(client.prepareSearch(INDEX)).thenReturn(queryBuilder);
        when(queryBuilder.execute()).thenReturn(listenableActionFuture);
        when(queryBuilder.setSize(any(Integer.class))).thenReturn(queryBuilder);
        when(queryBuilder.setQuery(any(QueryBuilder.class))).thenReturn(queryBuilder);
        when(listenableActionFuture.actionGet()).thenReturn(searchResponse);
        when(searchResponse.getHits()).thenReturn(hits);
    }

    @Test
    public void testLookup() {
        InternalSearchHit hit = new InternalSearchHit(23, "test", new StringText("test"), null);
        IndexRequest r = new IndexRequest();
        Map<String, String> test = new HashMap<>();
        test.put("definition", "test");
        r.source(test);
        hit.sourceRef(r.source());
        SearchHit[] hitsResult = {hit};
        when(hits.getHits()).thenReturn(hitsResult);
        List<TermResource> termResource = elasticSearchAutocompleteAPI.lookup(QUERY, 1);
        assertThat(termResource.size() == 1);
        assertThat(termResource.get(0).definition.equals("test"));
    }

    @Test
    public void testLookupWithEmptyResult() {
        SearchHit[] hitsResult = {};
        when(hits.getHits()).thenReturn(hitsResult);
        List<TermResource> termResource = elasticSearchAutocompleteAPI.lookup(QUERY, 1);
        assertThat(termResource.size() == 0);
    }

    @Test
    public void  testLookupWithTags(){
        SearchHit[] hitsResult = {};
        when(hits.getHits()).thenReturn(hitsResult);
        String[] tags = {"test"};
        List<TermResource> termResource = elasticSearchAutocompleteAPI.lookup(tags, QUERY, 1);
        assertThat(termResource.size() == 0);
    }



}
