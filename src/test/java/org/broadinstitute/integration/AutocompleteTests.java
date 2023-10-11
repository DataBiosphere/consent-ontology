package org.broadinstitute.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Autocomplete Related Tests")
class AutocompleteTests implements IntegrationTestHelper {

  @DisplayName("Autocomplete: OK test")
  @Test
  void ontology_autocomplete_page_OK() throws Exception {
    SimpleResponse response = fetchGetResponseWithQueryParam("autocomplete", "q", "term");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

}
