package org.broadinstitute.dsde.consent.ontology.resources;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


public class AutocompleteResourceTest {

    @Mock
    private AutocompleteService apiMockUp;

    private AutocompleteResource autocompleteResource;

    @Before
    public void setUp(){
        openMocks(this);
        autocompleteResource = new AutocompleteResource(apiMockUp);
    }

    @Test
    public void testGetTermWithOntologyNamesNull(){
        int limit = 1;
        List<TermResource> termResources = new ArrayList<>();

        Mockito.when(apiMockUp.lookup(null, limit)).thenReturn(termResources);
        autocompleteResource.getTerms(null, null, limit);
        verify(apiMockUp, times(1)).lookup(nullable(String.class), anyInt());
    }

    @Test
    public void testGetTermWithOntologyNames(){
        int limit = 20;
        List<TermResource> termResources = new ArrayList<>();
        String ontologyName = "Kidney Cancer";
        Mockito.when(apiMockUp.lookup(ontologyName, limit)).thenReturn(termResources);
        autocompleteResource.getTerms(null, null, limit);
        verify(apiMockUp,times(1)).lookup(nullable(String.class), anyInt());
    }

}
