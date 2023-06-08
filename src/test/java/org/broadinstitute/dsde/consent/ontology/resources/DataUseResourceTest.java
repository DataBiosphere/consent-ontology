package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class DataUseResourceTest {

  private DataUseResource dataUseResource;

  @BeforeEach
  public void setUp() {
    dataUseResource = new DataUseResource();
  }

  @Test
  public void testIndex() {
    Response response = dataUseResource.getSchema();
    assertStatusAndHeader(response, Response.Status.OK, MediaType.APPLICATION_JSON);
    String content = response.getEntity().toString().trim();
    assertTrue(content.contains("Data Use"));
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.readTree(content);
    } catch (Exception e) {
      fail("The response entity is not valid json");
    }
  }

  @SuppressWarnings("SameParameterValue")
  private void assertStatusAndHeader(Response response, Response.Status status,
      String contentType) {
    assertTrue(response.getStatus() == status.getStatusCode());
    Object header = response.getHeaders().get("Content-type");
    assertTrue(header.toString().contains(contentType));
  }

  @Test
  public void testValidateSchemaV3BadRequest() {
    try (Response response = dataUseResource.validateSchemaV3("{}")) {
      assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
  }

  @Test
  public void testValidateSchemaV3GoodRequest() {
    try (Response response = dataUseResource.validateSchemaV3("""
        {
        "generalUse": true,
        "diseaseRestrictions": ["test"],
        "hmbResearch": true,
        }
        """)) {
      assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
  }

}
