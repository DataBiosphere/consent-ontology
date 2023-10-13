package org.broadinstitute.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Data Use Related Tests")
class DataUseTests implements IntegrationTestHelper {

  private static final String dataUse = """
          {
            "hmbResearch": true
          }
      """;


  @DisplayName("Data Use V1 Schema: OK test")
  @Test
  void ontology_data_use_v1_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponse("schemas/data-use");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Data Use V2 Schema: OK test")
  @Test
  void ontology_data_use_v2_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponse("schemas/data-use/v2");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Data Use V3 Schema: OK test")
  @Test
  void ontology_data_use_v3_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponse("schemas/data-use/v3");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Data Use V3 Schema Validate: OK test")
  @Test
  void ontology_data_use_v3_validate_page_OK() throws Exception {
    SimpleResponse response = getPostResponse("schemas/data-use/v3", dataUse);
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

}
