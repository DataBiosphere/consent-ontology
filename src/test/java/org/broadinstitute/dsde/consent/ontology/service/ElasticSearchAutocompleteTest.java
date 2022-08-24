package org.broadinstitute.dsde.consent.ontology.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.api.client.http.HttpStatusCodes;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import org.broadinstitute.dsde.consent.ontology.WithMockServer;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.testcontainers.containers.MockServerContainer;

public class ElasticSearchAutocompleteTest implements WithMockServer {

    private ElasticSearchAutocomplete autocompleteAPI;
    private MockServerClient mockServerClient;
    private static final String INDEX_NAME = "local-ontology";
    private final MockServerContainer container = new MockServerContainer(IMAGE);

    @Mock
    private ElasticSearchSupport elasticSearchSupport;

    @BeforeEach
    void setUp() {
        container.start();
        openMocks(this);
        ElasticSearchConfiguration configuration = new ElasticSearchConfiguration();
        configuration.setIndex(INDEX_NAME);
        configuration.setServers(Collections.singletonList(container.getHost()));
        configuration.setPort(container.getServerPort());
        autocompleteAPI = new ElasticSearchAutocomplete(configuration);
        mockServerClient = new MockServerClient(container.getHost(), container.getServerPort());
    }

    @AfterEach
    void shutDown() {
        stop(container);
    }

    @Test
    public void testRetrySuccessAfterOneFailure() {
        mockServerClient.when(request(), Times.exactly(1)).error(error().withDropConnection(true));
        mockServerClient.when(request(), Times.exactly(1)).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assertions.assertEquals(1, termResource.size());
        Assertions.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void testRetrySuccessAfterTwoFailures() {
        mockServerClient.when(request(), Times.exactly(2)).error(error().withDropConnection(true));
        mockServerClient.when(request(), Times.exactly(1)).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assertions.assertEquals(1, termResource.size());
        Assertions.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void testRetryFailureAfterThreeFailures() {
        mockServerClient.when(request(), Times.exactly(3)).error(error().withDropConnection(true));
        mockServerClient.when(request(), Times.exactly(1)).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
        assertThrows(InternalServerErrorException.class, () -> {
            autocompleteAPI.lookup("cancer", 1);
        });
    }

    @Test
    public void testRetryFailure() {
        mockServerClient.when(request()).error(error().withDropConnection(true));
        assertThrows(InternalServerErrorException.class, () -> {
            autocompleteAPI.lookup("cancer", 1);
        });
    }

    @Test
    public void testBadRequest() {
        mockServerClient.when(request()).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST));
        assertThrows(BadRequestException.class, () -> {
            autocompleteAPI.lookup("cancer", 1);
        });
    }

    @Test
    public void testNotFound() {
        mockServerClient.when(request()).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_NOT_FOUND));
        assertThrows(NotFoundException.class, () -> {
            autocompleteAPI.lookup("cancer", 1);
        });
    }

    @Test
    public void testInvalidIdStringError() {
        mockServerClient.when(request()).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
        when(elasticSearchSupport.getEncodedEndpoint(anyString(), anyString())).thenThrow(new BadRequestException());
        autocompleteAPI.setElasticSearchSupport(elasticSearchSupport);
        assertThrows(BadRequestException.class, () -> {
            autocompleteAPI.lookupById("cancer");
        });
    }

    @Test
    public void testLookup() {
        mockServerClient.when(request()).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
        Assertions.assertEquals(1, termResource.size());
        Assertions.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void testLookupWithTags() {
        mockServerClient.when(request()).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
        List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("tag"), "cancer", 1);
        Assertions.assertEquals(1, termResource.size());
        Assertions.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    @Test
    public void  testLookupById() {
        mockServerClient.when(request()).respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerGetJson));
        List<TermResource> termResource = autocompleteAPI.lookupById("http://purl.obolibrary.org/obo/DOID_162");
        Assertions.assertEquals(1, termResource.size());
        Assertions.assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
    }

    // mock response for a search
    private static final String cancerJson = "{\n" +
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
    private static final String cancerGetJson = "{\n" +
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
