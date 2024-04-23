package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LivenessResourceTest {

  @Test
  void testHealthy() {
    LivenessResource resource = new LivenessResource();
    Response response = resource.getStatus();
    assertEquals(200, response.getStatus());
    assertEquals("Healthy!", response.getEntity());
  }

}
