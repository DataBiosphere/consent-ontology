package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.HashMap;
import org.broadinstitute.dsde.consent.ontology.datause.services.TextTranslationService;
import org.broadinstitute.dsde.consent.ontology.enumerations.TranslateFor;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TranslateResourceTest {

  @Mock
  private TextTranslationService service;
  private final Gson gson = new Gson();
  private TranslateResource resource;

  @BeforeEach
  void setUp() {
    resource = new TranslateResource(service);
  }

  @Test
  void testTranslateSummary() {
    String restriction = new DataUseBuilder().setGeneralUse(true).toString();
    try (Response response = resource.translateSummary(restriction)) {
      assertEquals(200, response.getStatus());
    }
  }

  @Test
  void testTranslateSummaryNoRestriction() {
    try (Response response = resource.translateSummary("")) {
      assertEquals(400, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testTranslateSummaryException() {
    when(service.translateDataUseSummary(any())).thenThrow(new RuntimeException());
    String restriction = new DataUseBuilder().setGeneralUse(true).toString();
    try (Response response = resource.translateSummary(restriction)) {
      assertEquals(500, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testDatasetTranslate() {
    DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
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
  void testTranslateDatasetException() throws Exception {
    when(service.translateDataset(any())).thenThrow(new IOException());
    DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
    try (Response response = resource.translate(
        TranslateFor.DATASET.name(),
        gson.toJson(datause))) {
      assertEquals(500, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testPurposeTranslate() {
    DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
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
  void testTranslatePurposeException() throws Exception {
    when(service.translatePurpose(any())).thenThrow(new IOException());
    DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
    try (Response response = resource.translate(
        TranslateFor.PURPOSE.name(),
        gson.toJson(datause))) {
      assertEquals(500, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testPurposeTranslateNoForParam() {
    DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
    try (Response response = resource.translate(
        null,
        gson.toJson(datause))) {
      assertEquals(400, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testPurposeTranslateNoForRestriction() {
    try (Response response = resource.translate(
        TranslateFor.PURPOSE.name(),
        "")) {
      assertEquals(400, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  void testTranslateParagraphBadRequest() {
    try (Response response = resource.translateParagraph("{}")) {
      assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
  }

  @Test
  void testTranslateParagraphServerError() {
    try (Response response = resource.translateParagraph("")) {
      assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
  }

  @Test
  void testTranslateParagraph() throws Exception {
    when(service.translateParagraph(any())).thenReturn(new HashMap<>());
    try (Response response = resource.translateParagraph("{\"paragraph\":\"Not for Profit\"}")) {
      assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
  }

  @Test
  void testParagraphTranslateException() throws Exception {
    when(service.translateParagraph(any())).thenThrow(new IOException());
    DataUse datause = new DataUseBuilder().setGeneralUse(true).build();
    try (Response response = resource.translate(
        TranslateFor.PARAGRAPH.name(),
        gson.toJson(datause))) {
      assertEquals(500, response.getStatus());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

}
