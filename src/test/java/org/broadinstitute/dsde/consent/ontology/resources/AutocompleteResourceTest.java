package org.broadinstitute.dsde.consent.ontology.resources;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AutocompleteResourceTest {

  @Mock
  private AutocompleteService apiMockUp;

  private AutocompleteResource autocompleteResource;

  @BeforeEach
  void setUp() {
    autocompleteResource = new AutocompleteResource(apiMockUp);
  }

  @Test
  void testGetTermWithOntologyNamesNull() {
    int limit = 1;
    List<TermResource> termResources = new ArrayList<>();

    when(apiMockUp.lookup(null, limit)).thenReturn(termResources);
    autocompleteResource.getTerms(null, null, limit);
    verify(apiMockUp, times(1)).lookup(nullable(String.class), anyInt());
  }

  @Test
  void testGetTermWithOntologyNames() {
    int limit = 20;
    List<TermResource> termResources = new ArrayList<>();

    when(apiMockUp.lookup(null, limit)).thenReturn(termResources);
    autocompleteResource.getTerms(null, null, limit);
    verify(apiMockUp, times(1)).lookup(nullable(String.class), anyInt());
  }

}
