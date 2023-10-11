package org.broadinstitute.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Translate Related Tests")
class TranslateTests implements IntegrationTestHelper {

  private static final String dataUse = """
          {
            "hmbResearch": true
          }
      """;

  @DisplayName("Translate for Dataset: OK test")
  @Test
  void ontology_translate_dataset_page_OK() throws Exception {
    SimpleResponse response = getPostResponseWithQueryParam("translate", dataUse, "for", "dataset");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Translate for Purpose: OK test")
  @Test
  void ontology_translate_purpose_page_OK() throws Exception {
    SimpleResponse response = getPostResponseWithQueryParam("translate", dataUse, "for", "purpose");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Translate Paragraph: OK Test")
  @Test
  // BEE related tests do not have access to GCP resource required for paragraph translation
  @Disabled
  void ontology_translate_paragraph_page_OK() throws Exception {
    String paragraph = """
        {
          "paragraph": "string"
        }
        """;
    SimpleResponse response = getPostResponse("translate/paragraph", paragraph);
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Translate Summary: OK test")
  @Test
  void ontology_translate_summary_page_OK() throws Exception {
    SimpleResponse response = getPostResponse("translate/summary", dataUse);
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

}
