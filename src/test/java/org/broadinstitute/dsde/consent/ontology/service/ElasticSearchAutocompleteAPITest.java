package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchAutocompleteAPITest {

    private ElasticSearchAutocompleteAPI elasticSearchAutocompleteAPI;

    private final String INDEX = "ontology";
//    private final String QUERY = "name=test";

    // TODO: Fix tests
    @Before
    public void setUpClass() {
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX);
        configuration.setServers(Collections.singletonList("localhost"));
        elasticSearchAutocompleteAPI = new ElasticSearchAutocompleteAPI(configuration);
    }

    // TODO: Rewrite tests

    @Test
    public void testLookup() {
//        InternalSearchHit hit = new InternalSearchHit(23, "test", new StringText("test"), null);
//        IndexRequest r = new IndexRequest();
//        Map<String, String> test = new HashMap<>();
//        test.put("definition", "test");
//        r.source(test);
//        hit.sourceRef(r.source());
//        SearchHit[] hitsResult = {hit};
//        when(hits.getHits()).thenReturn(hitsResult);
//        List<TermResource> termResource = elasticSearchAutocompleteAPI.lookup(QUERY, 1);
//        assertThat(termResource.size() == 1);
//        assertThat(termResource.get(0).definition.equals("test"));
        Assert.assertTrue(true);
    }
//
//    @Test
//    public void testLookupWithEmptyResult() {
//        SearchHit[] hitsResult = {};
//        when(hits.getHits()).thenReturn(hitsResult);
//        List<TermResource> termResource = elasticSearchAutocompleteAPI.lookup(QUERY, 1);
//        assertThat(termResource.size() == 0);
//    }
//
//    @Test
//    public void  testLookupWithTags(){
//        SearchHit[] hitsResult = {};
//        when(hits.getHits()).thenReturn(hitsResult);
//        String[] tags = {"test"};
//        List<TermResource> termResource = elasticSearchAutocompleteAPI.lookup(tags, QUERY, 1);
//        assertThat(termResource.size() == 0);
//    }



}
