package org.broadinstitute.dsde.consent.ontology.resources;

import org.junit.Test;
import javax.ws.rs.core.Response;
import static org.junit.Assert.assertEquals;

public class LivenessResourceTest {
    @Test
    public void testHealthy() {
        LivenessResource resource = new LivenessResource();
        Response response = resource.getStatus();
        assertEquals(200, response.getStatus());
        assertEquals("Healthy!", response.getEntity());
    }

}
