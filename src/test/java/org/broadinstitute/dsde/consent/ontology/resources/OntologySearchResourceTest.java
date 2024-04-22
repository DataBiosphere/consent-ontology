package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OntologySearchResourceTest {

  @Mock
  private AutocompleteService autocompleteService;

  private OntologySearchResource resource;
  private final List<TermResource> nonEmptyTermList = new ArrayList<>();
  private final List<TermResource> emptyTermList = new ArrayList<>();

  @BeforeEach
  void setUp() {
    resource = new OntologySearchResource(autocompleteService);
    TermResource term = new TermResource();
    term.id = "DOID_4";
    term.label = "Some label";
    term.definition = "Some definition";
    nonEmptyTermList.add(term);
  }

  @Test
  void testGetByIdBadRequest() throws Exception {
    Response response = resource.getOntologyById("");
    assertEquals(400, response.getStatus());
    ErrorResponse error = (ErrorResponse) response.getEntity();
    assertEquals("Ontology ID term cannot be empty.", error.getMessage());
    verify(autocompleteService, times(0)).lookupById(Mockito.anyString());
  }

  @Test
  void testGetByIdNotFound() throws Exception {
    Response response = resource.getOntologyById("DOID_404");
    assertEquals(404, response.getStatus());
    ErrorResponse error = (ErrorResponse) response.getEntity();
    assertEquals("Supplied IDs do not match any known ontologies.", error.getMessage());
    verify(autocompleteService, times(1)).lookupById("DOID_404");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testGetById() throws Exception {
    when(autocompleteService.lookupById("DOID_4")).thenReturn(nonEmptyTermList);
    Response response = resource.getOntologyById("DOID_4");
    assertEquals(200, response.getStatus());
    List<TermResource> terms = (List<TermResource>) response.getEntity();
    assertEquals(1, terms.size());
    assertEquals("DOID_4", terms.get(0).id);
    assertEquals("Some label", terms.get(0).label);
    assertEquals("Some definition", terms.get(0).definition);
    verify(autocompleteService, times(1)).lookupById("DOID_4");
  }

  @Test
  void testGetByIdMultiple() throws Exception {
    when(autocompleteService.lookupById("DOID_4")).thenReturn(nonEmptyTermList);
    when(autocompleteService.lookupById("DOID_5")).thenReturn(emptyTermList);
    when(autocompleteService.lookupById("DOID_6")).thenReturn(emptyTermList);
    Response response = resource.getOntologyById("DOID_4,DOID_5,DOID_6");
    assertEquals(200, response.getStatus());
    verify(autocompleteService, times(3)).lookupById(Mockito.anyString());
  }

  @Test
  void testGetByIdMultipleEmpty() throws Exception {
    Response response = resource.getOntologyById(",,,");
    assertEquals(404, response.getStatus());
    ErrorResponse error = (ErrorResponse) response.getEntity();
    assertEquals("Supplied IDs do not match any known ontologies.", error.getMessage());
    verify(autocompleteService, times(0)).lookupById(Mockito.anyString());
  }

  @Test
  void testGetByIdMultipleNotFound() throws Exception {
    Response response = resource.getOntologyById(",DOID_404,");
    assertEquals(404, response.getStatus());
    ErrorResponse error = (ErrorResponse) response.getEntity();
    assertEquals("Supplied IDs do not match any known ontologies.", error.getMessage());
    verify(autocompleteService, times(1)).lookupById("DOID_404");
  }

  @SuppressWarnings("unchecked")
  @Test
  void testGetByIdSomeFound() throws Exception {
    when(autocompleteService.lookupById("DOID_4")).thenReturn(nonEmptyTermList);
    when(autocompleteService.lookupById("DOID_3")).thenReturn(emptyTermList);
    Response response = resource.getOntologyById(",DOID_3,,DOID_4");
    assertEquals(200, response.getStatus());
    List<TermResource> terms = (List<TermResource>) response.getEntity();
    assertEquals(1, terms.size());
    assertEquals("DOID_4", terms.get(0).id);
    assertEquals("Some label", terms.get(0).label);
    assertEquals("Some definition", terms.get(0).definition);
    verify(autocompleteService, times(1)).lookupById("DOID_4");
    verify(autocompleteService, times(2)).lookupById(Mockito.anyString());
  }

}
