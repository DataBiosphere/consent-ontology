package org.broadinstitute.dsde.consent.ontology.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.api.client.http.HttpStatusCodes;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.WithMockServer;
import org.broadinstitute.dsde.consent.ontology.configurations.ElasticSearchConfiguration;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.testcontainers.containers.MockServerContainer;

@ExtendWith(MockitoExtension.class)
class ElasticSearchAutocompleteTest implements WithMockServer {

  private ElasticSearchAutocomplete autocompleteAPI;
  private MockServerClient mockServerClient;
  private static final String INDEX_NAME = "local-ontology";
  private final MockServerContainer container = new MockServerContainer(IMAGE);

  @Mock
  private ElasticSearchSupport elasticSearchSupport;

  @BeforeEach
  void setUp() {
    container.start();
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
  void testRetrySuccessAfterOneFailure() {
    mockServerClient.when(request(), Times.exactly(1)).error(error().withDropConnection(true));
    mockServerClient.when(request(), Times.exactly(1))
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
    List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
    assertEquals(1, termResource.size());
    assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
  }

  @Test
  void testRetrySuccessAfterTwoFailures() {
    mockServerClient.when(request(), Times.exactly(2)).error(error().withDropConnection(true));
    mockServerClient.when(request(), Times.exactly(1))
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
    List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
    assertEquals(1, termResource.size());
    assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
  }

  @Test
  void testRetryFailureAfterThreeFailures() {
    mockServerClient.when(request(), Times.exactly(3)).error(error().withDropConnection(true));
    mockServerClient.when(request(), Times.exactly(1))
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
    assertThrows(InternalServerErrorException.class, () -> {
      autocompleteAPI.lookup("cancer", 1);
    });
  }

  @Test
  void testRetryFailure() {
    mockServerClient.when(request()).error(error().withDropConnection(true));
    assertThrows(InternalServerErrorException.class, () -> {
      autocompleteAPI.lookup("cancer", 1);
    });
  }

  @Test
  void testBadRequest() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_BAD_REQUEST));
    assertThrows(BadRequestException.class, () -> {
      autocompleteAPI.lookup("cancer", 1);
    });
  }

  @Test
  void testNotFound() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_NOT_FOUND));
    assertThrows(NotFoundException.class, () -> {
      autocompleteAPI.lookup("cancer", 1);
    });
  }

  @Test
  void testInvalidIdStringError() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
    when(elasticSearchSupport.getEncodedEndpoint(anyString(), anyString())).thenThrow(
        new BadRequestException());
    autocompleteAPI.setElasticSearchSupport(elasticSearchSupport);
    assertThrows(BadRequestException.class, () -> {
      autocompleteAPI.lookupById("cancer");
    });
  }

  @Test
  void testLookup() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
    List<TermResource> termResource = autocompleteAPI.lookup("cancer", 1);
    assertEquals(1, termResource.size());
    assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
  }

  @Test
  void testLookupWithTags() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerJson));
    List<TermResource> termResource = autocompleteAPI.lookup(Collections.singletonList("tag"),
        "cancer", 1);
    assertEquals(1, termResource.size());
    assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
  }

  @Test
  void testLookupById() {
    mockServerClient.when(request())
        .respond(response().withStatusCode(HttpStatusCodes.STATUS_CODE_OK).withBody(cancerGetJson));
    List<TermResource> termResource = autocompleteAPI.lookupById(
        "http://purl.obolibrary.org/obo/DOID_162");
    assertEquals(1, termResource.size());
    assertTrue(termResource.get(0).getSynonyms().contains("primary cancer"));
  }

  // mock response for a search
  private static final String cancerJson = """
      {
        "took": 15,
        "timed_out": false,
        "_shards": {
          "total": 5,
          "successful": 5,
          "failed": 0
        },
        "hits": {
          "total": 1,
          "max_score": 100,
          "hits": [
            {
              "_index": "local-ontology",
              "_type": "ontology_term",
              "_id": "http://purl.obolibrary.org/obo/DOID_162",
              "_score": 21.416782,
              "_source": {
                "id": "http://purl.obolibrary.org/obo/DOID_162",
                "ontology": "Disease",
                "synonyms": [
                  "primary cancer",
                  "malignant tumor ",
                  "malignant neoplasm"
                ],
                "label": "cancer",
                "definition": "A disease of cellular proliferation that is malignant and primary, characterized by uncontrolled cellular proliferation, local cell invasion and metastasis.",
                "usable": true,
                "parents": [
                  {
                    "id": "http://purl.obolibrary.org/obo/DOID_14566",
                    "order": 1
                  },
                  {
                    "id": "http://purl.obolibrary.org/obo/DOID_4",
                    "order": 2
                  }
                ]
              }
            }
          ]
        }
      }""";

  // mock response for a document get-by-id
  private static final String cancerGetJson = """
      {
        "_index": "ontology",
        "_type": "ontology_term",
        "_id": "http://purl.obolibrary.org/obo/DOID_162",
        "_version": 32,
        "found": true,
        "_source": {
          "id": "http://purl.obolibrary.org/obo/DOID_162",
          "ontology": "Disease",
          "synonyms": [
            "primary cancer",
            "malignant tumor ",
            "malignant neoplasm"
          ],
          "label": "cancer",
          "definition": "A disease of cellular proliferation that is malignant and primary, characterized by uncontrolled cellular proliferation, local cell invasion and metastasis.",
          "usable": true,
          "parents": [
            {
              "id": "http://purl.obolibrary.org/obo/DOID_14566",
              "order": 1
            },
            {
              "id": "http://purl.obolibrary.org/obo/DOID_4",
              "order": 2
            }
          ]
        }
      }""";

}
