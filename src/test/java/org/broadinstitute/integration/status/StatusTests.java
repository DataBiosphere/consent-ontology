package org.broadinstitute.integration.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.api.client.http.HttpStatusCodes;
import org.broadinstitute.integration.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Status Related Tests")
class StatusTests implements IntegrationTestHelper {

  @DisplayName("Status: Status test")
  @Test
  void ontology_status_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponse("status");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Status: Liveness test")
  @Test
  void ontology_liveness_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponse("liveness");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Status: Version test")
  @Test
  void ontology_version_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponse("version");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

}
