package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.io.IOException;
import java.util.HashMap;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.broadinstitute.dsde.consent.ontology.cloudstore.GCSStore;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.service.MockTranslationService;
import org.broadinstitute.dsde.consent.ontology.translate.service.Translate;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;

public class TranslateResourceTest {

    private static final TextTranslationService mockService = new MockTranslationService();

    @Mock
    private static GCSStore gcsStore;

    @Mock
    private static Translate translate = new Translate(gcsStore);

    private static ObjectMapper mapper = new ObjectMapper();

    @ClassRule
    public static final ResourceTestRule gRule = ResourceTestRule
            .builder()
            .addResource(new TranslateResource(mockService, translate, mapper))
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
    public void testParagraphTranslate() throws Exception {
        String paragraph = "{\"paragraph\": \"This could be a disease or anything else.\"}";
        TranslateResource resource = new TranslateResource(mockService, translate, mapper);
        Response response = resource.translateParagraph(paragraph);
        assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.getStatus());
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
