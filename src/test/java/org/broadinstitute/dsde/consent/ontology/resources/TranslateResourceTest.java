package org.broadinstitute.dsde.consent.ontology.resources;

import com.google.gson.Gson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.broadinstitute.dsde.consent.ontology.service.MockTranslationService;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TranslateResourceTest {

    private static final TextTranslationService mockTranslationService = new MockTranslationService();

    @InjectMocks
    private static TranslateResource translateResource = new TranslateResource();

    @ClassRule
    public static final ResourceTestRule gRule = ResourceTestRule
            .builder()
            .addResource(translateResource)
            .build();


    @BeforeClass
    public static void testInTempFolder() {
        translateResource.setTranslationService(mockTranslationService);
    }

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
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo(mockTranslationService.translateDatasetDataUse(""));
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
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.readEntity(String.class)).isEqualTo(mockTranslationService.translatePurposeDataUse(""));
    }

}
