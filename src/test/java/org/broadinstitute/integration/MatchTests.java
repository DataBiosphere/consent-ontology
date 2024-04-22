package org.broadinstitute.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.api.client.http.HttpStatusCodes;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Match Related Tests")
class MatchTests implements IntegrationTestHelper {

  private static final String matchPair = """
      {
        "purpose": {
          "generalUse": false,
          "hmbResearch": true
        },
        "consent": {
          "generalUse": true
        }
      }
      """;

  @DisplayName("Match V2: OK test")
  @Test
  void ontology_match_v2_page_OK() throws Exception {
    SimpleResponse response = getPostResponse("match/v2", matchPair);
    assertEquals(Status.GONE.getStatusCode(), response.code());
  }

  @DisplayName("Match V3: OK test")
  @Test
  void ontology_match_v3_page_OK() throws Exception {
    SimpleResponse response = getPostResponse("match/v3", matchPair);
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Match V4: OK test")
  @Test
  void ontology_match_v4_page_OK() throws Exception {
    SimpleResponse response = getPostResponse("match/v3", matchPair);
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

}
