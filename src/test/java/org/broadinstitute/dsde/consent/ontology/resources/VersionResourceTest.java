package org.broadinstitute.dsde.consent.ontology.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import javax.ws.rs.core.Response;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

public class VersionResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new VersionResource())
            .build();


    @Test
    public void testGetVersion() {
        Response response = resources.target("/version").request().get();
        Assert.assertEquals(200, response.getStatus());
    }

}
