package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.service.MockTranslationService;
import org.junit.ClassRule;
import org.junit.Test;

public class TranslateResourceTest {

    private static final TextTranslationService mockService = new MockTranslationService();

    @ClassRule
    public static final ResourceTestRule gRule = ResourceTestRule
            .builder()
            .addResource(new TranslateResource(mockService))
            .build();

    @Test
    public void testDatasetTranslate() throws IOException {
        DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
        Gson gson = new Gson();
        Response response =
                gRule.client().target("/translate")
                        .queryParam("for", TextTranslationService.TranslateFor.DATASET.name())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .accept(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.json(gson.toJson(datause)));
        assertEquals(200, response.getStatus());
        assertEquals(response.readEntity(String.class), mockService.translateDataset(""));
    }

    @Test
    public void testPurposeTranslate() throws IOException {
        DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
        Gson gson = new Gson();
        Response response =
                gRule.client().target("/translate")
                        .queryParam("for", TextTranslationService.TranslateFor.PURPOSE.name())
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .accept(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.json(gson.toJson(datause)));
        assertEquals(200, response.getStatus());
        assertEquals(response.readEntity(String.class), mockService.translatePurpose(""));
    }

}
