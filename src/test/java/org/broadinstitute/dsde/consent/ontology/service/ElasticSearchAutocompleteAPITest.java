package org.broadinstitute.dsde.consent.ontology.service;

import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpResponse;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SuppressWarnings("FieldCanBeLocal")
public class ElasticSearchAutocompleteAPITest {

    private ElasticSearchAutocompleteAPI autocompleteAPI;
    private static final String INDEX_NAME = "local-ontology";
    private ClientAndServer server;

    @Mock
    ElasticSearchSupport elasticSearchSupport;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX_NAME);
        configuration.setServers(Collections.singletonList("localhost"));
        autocompleteAPI = new ElasticSearchAutocompleteAPI(configuration);
        server = startClientAndServer(9200);
    }

    @After
    public void shutDown() {
        if (server != null && server.isRunning()) {
            server.stop();
        }
    }

    private void mockResponse(HttpResponse response) {
        server.reset();
        server.when(request()).respond(response);
    }

    @Test
    public void testRetrySuccessAfterOneFailure() {
        server.reset();
        server.when(request(), Times.exactly(1)).error(error().withDropConnection(true));
        server.when(request(), Times.exactly(1)).respond(response().withStatusCode(200).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assert.assertEquals(1, termResource.size());
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void testRetrySuccessAfterTwoFailures() {
        server.reset();
        server.when(request(), Times.exactly(2)).error(error().withDropConnection(true));
        server.when(request(), Times.exactly(1)).respond(response().withStatusCode(200).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assert.assertEquals(1, termResource.size());
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test(expected = InternalServerErrorException.class)
    public void testRetryFailureAfterThreeFailures() {
        server.reset();
        server.when(request(), Times.exactly(3)).error(error().withDropConnection(true));
        server.when(request(), Times.exactly(1)).respond(response().withStatusCode(200).withBody(cancerJson));
        autocompleteAPI.lookup("cancer", 1);
    }

    @Test(expected = InternalServerErrorException.class)
    public void testRetryFailure() {
        server.reset();
        server.when(request()).error(error().withDropConnection(true));
        autocompleteAPI.lookup("cancer", 1);
    }

    @Test(expected = InternalServerErrorException.class)
    public void testBadResponse() {
        mockResponse(response().withStatusCode(400));
        autocompleteAPI.lookup("cancer", 1);
    }

    @Test(expected = BadRequestException.class)
    public void testInvalidIdStringError() {
        mockResponse(response().withStatusCode(200).withBody(cancerJson));
        when(elasticSearchSupport.getEncodedEndpoint(any(), any())).thenThrow(new BadRequestException());
        autocompleteAPI.setElasticSearchSupport(elasticSearchSupport);
        autocompleteAPI.lookupById("cancer");
    }

    @Test
    public void testLookup() {
        mockResponse(response().withStatusCode(200).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assert.assertEquals(1, termResource.size());
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void testLookupWithTags() {
        mockResponse(response().withStatusCode(200).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("tag"), "cancer", 1);
        Assert.assertEquals(1, termResource.size());
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void  testLookupById() {
        mockResponse(response().withStatusCode(200).withBody(cancerGetJson));
        List<TermResource> termResource = autocompleteAPI.lookupById("http://purl.obolibrary.org/obo/DOID_162");
        Assert.assertEquals(1, termResource.size());
        Assert.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    // mock response for a search
    private static String cancerJson = "{\n" +
        "  \"took\": 15,\n" +
        "  \"timed_out\": false,\n" +
        "  \"_shards\": {\n" +
        "    \"total\": 5,\n" +
        "    \"successful\": 5,\n" +
        "    \"failed\": 0\n" +
        "  },\n" +
        "  \"hits\": {\n" +
        "    \"total\": 1,\n" +
        "    \"max_score\": 100,\n" +
        "    \"hits\": [\n" +
        "      {\n" +
        "        \"_index\": \"local-ontology\",\n" +
        "        \"_type\": \"ontology_term\",\n" +
        "        \"_id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
        "        \"_score\": 21.416782,\n" +
        "        \"_source\": {\n" +
        "          \"id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
        "          \"ontology\": \"Disease\",\n" +
        "          \"synonyms\": [\n" +
        "            \"primary cancer\",\n" +
        "            \"malignant tumor \",\n" +
        "            \"malignant neoplasm\"\n" +
        "          ],\n" +
        "          \"label\": \"cancer\",\n" +
        "          \"definition\": \"A disease of cellular proliferation that is malignant and primary, characterized by uncontrolled cellular proliferation, local cell invasion and metastasis.\",\n" +
        "          \"usable\": true,\n" +
        "          \"parents\": [\n" +
        "            {\n" +
        "              \"id\": \"http://purl.obolibrary.org/obo/DOID_14566\",\n" +
        "              \"order\": 1\n" +
        "            },\n" +
        "            {\n" +
        "              \"id\": \"http://purl.obolibrary.org/obo/DOID_4\",\n" +
        "              \"order\": 2\n" +
        "            }\n" +
        "          ]\n" +
        "        }\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}";

    // mock response for a document get-by-id
    private static String cancerGetJson = "{\n" +
            "  \"_index\": \"ontology\",\n" +
            "  \"_type\": \"ontology_term\",\n" +
            "  \"_id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
            "  \"_version\": 32,\n" +
            "  \"found\": true,\n" +
            "  \"_source\": {\n" +
            "    \"id\": \"http://purl.obolibrary.org/obo/DOID_162\",\n" +
            "    \"ontology\": \"Disease\",\n" +
            "    \"synonyms\": [\n" +
            "      \"primary cancer\",\n" +
            "      \"malignant tumor \",\n" +
            "      \"malignant neoplasm\"\n" +
            "    ],\n" +
            "    \"label\": \"cancer\",\n" +
            "    \"definition\": \"A disease of cellular proliferation that is malignant and primary, characterized by uncontrolled cellular proliferation, local cell invasion and metastasis.\",\n" +
            "    \"usable\": true,\n" +
            "    \"parents\": [\n" +
            "      {\n" +
            "        \"id\": \"http://purl.obolibrary.org/obo/DOID_14566\",\n" +
            "        \"order\": 1\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": \"http://purl.obolibrary.org/obo/DOID_4\",\n" +
            "        \"order\": 2\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

}
