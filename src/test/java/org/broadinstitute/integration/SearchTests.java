package org.broadinstitute.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.api.client.http.HttpStatusCodes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Search Related Tests")
class SearchTests implements IntegrationTestHelper {

  @DisplayName("Search Page: OK Test")
  @Test
  // BEE related integration tests do not currently have a populated ES index
  @Disabled
  void ontology_search_page_ok() throws Exception {
    SimpleResponse response = fetchGetResponseWithQueryParam("search", "id", "DOID_162");
    assertEquals(HttpStatusCodes.STATUS_CODE_OK, response.code());
  }

  @DisplayName("Search Page: Not Found Test")
  @Test
  void ontology_search_page_not_found() throws Exception {
    SimpleResponse response = fetchGetResponseWithQueryParam("search", "id", "invalid_term");
    assertEquals(HttpStatusCodes.STATUS_CODE_NOT_FOUND, response.code());
  }

}
