package org.broadinstitute.dsde.consent.ontology.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.ElasticSearchAutocompleteAPI;
import org.elasticsearch.client.RestClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Integration test classes use the maven integration framework to start up an elastic search instance.
 * Tests can be run locally if a local docker es instance is manually started up:
 * docker run -p 9200:9200 -e "xpack.security.enabled=false" -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" docker.elastic.co/elasticsearch/elasticsearch:5.5.0
 */
@Category(IntegrationTest.class)
public class ElasticSearchAutocompleteAPITest {

    private static ElasticSearchAutocompleteAPI autocompleteAPI;
    private static final String INDEX_NAME = "ontology-integration";
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchAutocompleteAPITest.class);
    private static Gson gson = new GsonBuilder().create();

    @BeforeClass
    public static void setUp() throws Exception {
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX_NAME);
        configuration.setServers(Collections.singletonList("localhost"));
        autocompleteAPI = new ElasticSearchAutocompleteAPI(configuration);
        RestClient client = org.broadinstitute.dsde.consent.ontology.service.ElasticSearchSupport.createRestClient(configuration);
        IndexSupport.createIndex(client, INDEX_NAME);
        IndexSupport.populateIndex(client, INDEX_NAME);
    }

    @After
    public void shutDown() throws Exception {
    }

    @Test
    public void testLookup() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        logTerms("testLookup", termResource);
        Assert.assertTrue(termResource.size() == 1);
        List<String> synonyms = splitSynonymList(termResource.get(0).getSynonyms());
        Assert.assertTrue(synonyms.contains("cancer"));
    }

    @Test
    public void testLookupWithTags() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("Disease"), "cancer", 1);
        logTerms("testLookupWithTags", termResource);
        Assert.assertTrue(termResource.size() == 1);
        List<String> synonyms = splitSynonymList(termResource.get(0).getSynonyms());
        Assert.assertTrue(synonyms.contains("cancer"));
    }

    @Test
    public void testLookupWithWrongTags() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("Organization"), "cancer", 1);
        logTerms("testLookupWithWrongTags", termResource);
        Assert.assertTrue(termResource.size() == 0);
    }

    @Test
    public void testLookupWithCount() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 7);
        logTerms("testLookupWithCount", termResource);
        Assert.assertTrue(termResource.size() == 7);
    }

    @Test
    public void  testLookupById() throws Exception {
        List<TermResource> termResource = autocompleteAPI.lookupById("http://purl.obolibrary.org/obo/DOID_162");
        logTerms("testLookupById", termResource);
        Assert.assertNotNull(termResource);
    }

    private static List<String> splitSynonymList(List<String> synonyms) {
        return synonyms.
            stream().
            map(s -> s.split(" ")).
            flatMap(Arrays::stream).
            distinct().
            collect(Collectors.toList());
    }

    private static void logTerms(String testName, List<TermResource> termResources) {
        termResources.forEach( t -> logger.debug(testName + ": " + gson.toJson(t)));
    }

}
