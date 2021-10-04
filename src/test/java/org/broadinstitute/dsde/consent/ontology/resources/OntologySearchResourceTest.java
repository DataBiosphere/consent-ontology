package org.broadinstitute.dsde.consent.ontology.resources;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@SuppressWarnings("SimplifiableJUnitAssertion")
@RunWith(MockitoJUnitRunner.class)
public class OntologySearchResourceTest {

    @Mock
    AutocompleteService autocompleteService;

    private OntologySearchResource resource;
    private List<TermResource> nonEmptyTermList = new ArrayList<>();
    private List<TermResource> emptyTermList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        openMocks(this);
        resource = new OntologySearchResource(autocompleteService);
        TermResource term = new TermResource();
        term.id = "DOID_4";
        term.label = "Some label";
        term.definition = "Some definition";
        nonEmptyTermList.add(term);
        Mockito.when(autocompleteService.lookupById("DOID_4")).thenReturn(nonEmptyTermList);
        Mockito.when(autocompleteService.lookupById("DOID_404")).thenReturn(emptyTermList);
    }

    @Test
    public void testGetByIdBadRequest() throws Exception {
        Response response = resource.getOntologyById("");
        assertTrue(response.getStatus() == 400);
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertTrue(error.getMessage().equals(" Ontology ID term cannot be empty. "));
        verify(autocompleteService, times(0)).lookupById(Mockito.anyString());
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        Response response = resource.getOntologyById("DOID_404");
        assertTrue(response.getStatus() == 404);
        ErrorResponse error = (ErrorResponse) response.getEntity();
        assertTrue(error.getMessage().equals(" Supplied ID doesn't match any known ontology. "));
        verify(autocompleteService, times(1)).lookupById("DOID_404");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetById() throws Exception {
        Response response = resource.getOntologyById("DOID_4");
        assertTrue(response.getStatus() == 200);
        List<TermResource> terms = (List<TermResource>) response.getEntity();
        assertTrue(terms.size() == 1);
        assertTrue(terms.get(0).id.equals("DOID_4"));
        assertTrue(terms.get(0).label.equals("Some label"));
        assertTrue(terms.get(0).definition.equals("Some definition"));
        verify(autocompleteService, times(1)).lookupById("DOID_4");
    }

}
