package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;
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
            new JSONObject(content);
        } catch (JSONException e) {
            fail("The response entity is not valid json");
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void assertStatusAndHeader(Response response, Response.Status status, String contentType) {
        assertTrue(response.getStatus() == status.getStatusCode());
        Object header = response.getHeaders().get("Content-type");
        assertTrue(header.toString().contains(contentType));
    }

}
