package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.Assert.fail;
import static org.mockito.MockitoAnnotations.openMocks;

import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VersionResourceTest {

    VersionResource resource;
    @BeforeEach
    public void setUp(){
        openMocks(this);
        resource = new VersionResource();
    }


    @Test
    public void testGetVersion() {
        try(Response response = resource.content()) {
            Assert.assertEquals(200, response.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
