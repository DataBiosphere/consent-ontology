package org.broadinstitute.dsde.consent.ontology.resources;

import static org.broadinstitute.dsde.consent.ontology.resources.SwaggerResource.MEDIA_TYPE_JS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SwaggerResourceTest {

  private SwaggerResource swaggerResource;

  @BeforeEach
  public void setUp() {
    swaggerResource = new SwaggerResource();
  }

  @Test
  public void testIndex() {
    try (Response response = swaggerResource.content("index.html")) {
      checkStatusAndHeader(response, MediaType.TEXT_HTML);
      String content = response.getEntity().toString().trim();
      assertTrue(content.contains("<!DOCTYPE html>"));
      assertTrue(content.contains("</html>"));
    }
  }

  @Test
  public void testInitializer() {
    try (Response response = swaggerResource.content("swagger-initializer.js")) {
      checkStatusAndHeader(response, MEDIA_TYPE_JS);
      String content = response.getEntity().toString().trim();
      assertTrue(content.startsWith("window.onload"));
    }
  }

  @Test
  public void testNotFound() {
    try (Response response = swaggerResource.content("foo/bar.txt")) {
      assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }
  }

  @Test
  public void testImageNotFound() {
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
