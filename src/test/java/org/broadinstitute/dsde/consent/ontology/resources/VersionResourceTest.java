package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.MockitoAnnotations.openMocks;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VersionResourceTest {

  VersionResource resource;

  @BeforeEach
  void setUp() {
    resource = new VersionResource();
  }


  @Test
  void testGetVersion() {
    try (Response response = resource.content()) {
      assertEquals(200, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

}
