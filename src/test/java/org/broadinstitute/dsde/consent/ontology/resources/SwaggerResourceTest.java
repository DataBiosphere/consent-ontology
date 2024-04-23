package org.broadinstitute.dsde.consent.ontology.resources;

import static org.broadinstitute.dsde.consent.ontology.resources.SwaggerResource.MEDIA_TYPE_JS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SwaggerResourceTest {

  private SwaggerResource swaggerResource;

  @BeforeEach
  void setUp() {
    swaggerResource = new SwaggerResource();
  }

  @Test
  void testIndex() {
    try (Response response = swaggerResource.content("index.html")) {
      checkStatusAndHeader(response, MediaType.TEXT_HTML);
      String content = response.getEntity().toString().trim();
      assertTrue(content.contains("<!DOCTYPE html>"));
      assertTrue(content.contains("</html>"));
    }
  }

  @Test
  void testInitializer() {
    try (Response response = swaggerResource.content("swagger-initializer.js")) {
      checkStatusAndHeader(response, MEDIA_TYPE_JS);
      String content = response.getEntity().toString().trim();
      assertTrue(content.startsWith("window.onload"));
    }
  }

  @Test
  void testNotFound() {
    try (Response response = swaggerResource.content("foo/bar.txt")) {
      assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }
  }

  @Test
  void testImageNotFound() {
    try (Response response = swaggerResource.content("foo/bar.png")) {
      assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }
  }

  private void checkStatusAndHeader(Response response, String header) {
    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    String headerObject = response.getHeaderString(HttpHeaders.CONTENT_TYPE);
    assertTrue(headerObject.contains(header));
  }
}
