package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.gson.Gson;
import java.util.HashMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.enumerations.TranslateFor;
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
            TranslateFor.DATASET.name(),
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
            TranslateFor.PURPOSE.name(),
            gson.toJson(datause))) {
            assertEquals(200, response.getStatus());
            verify(service, atLeastOnce()).translatePurpose(any());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateParagraphBadRequest() {
      try (Response response = resource.translateParagraph("{}")) {
        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
      }
    }

    @Test
    public void testTranslateParagraphServerError() {
      try (Response response = resource.translateParagraph("")) {
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
      }
    }

    @Test
    public void testTranslateParagraph() throws Exception {
      when(service.translateParagraph(any())).thenReturn(new HashMap<>());
      try (Response response = resource.translateParagraph("{\"paragraph\":\"Not for Profit\"}")) {
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
      }
    }

}
