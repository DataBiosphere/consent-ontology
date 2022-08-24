package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.gson.Gson;
import javax.ws.rs.core.Response;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class TranslateResourceTest {

    @Mock
    private TextTranslationService service;
    private final Gson gson = new Gson();
    private TranslateResource resource;

    @BeforeEach
    public void setUp(){
        openMocks(this);
        resource = new TranslateResource(service);
    }

    @Test
    public void testDatasetTranslate() {
        DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
        spy(service);
        try (Response response = resource.translate(
            TextTranslationService.TranslateFor.DATASET.name(),
            gson.toJson(datause))) {
            assertEquals(200, response.getStatus());
            verify(service, atLeastOnce()).translateDataset(any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPurposeTranslate() {
        DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
        spy(service);
        try (Response response = resource.translate(
            TextTranslationService.TranslateFor.PURPOSE.name(),
            gson.toJson(datause))) {
            assertEquals(200, response.getStatus());
            verify(service, atLeastOnce()).translatePurpose(any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
