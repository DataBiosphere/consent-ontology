package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

public class LivenessResourceTest {
    @Test
    public void testHealthy() {
        LivenessResource resource = new LivenessResource();
        Response response = resource.getStatus();
        assertEquals(200, response.getStatus());
        assertEquals("Healthy!", response.getEntity());
    }

}
