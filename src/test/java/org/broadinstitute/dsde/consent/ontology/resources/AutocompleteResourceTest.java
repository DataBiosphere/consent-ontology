package org.broadinstitute.dsde.consent.ontology.resources;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AutocompleteResourceTest {

    @Mock
    AutocompleteService apiMockUp;

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

        Mockito.when(apiMockUp.lookup(null,limit)).thenReturn(termResources);
        autocompleteResource.getTerms(null, null, limit);
        verify(apiMockUp, times(1)).lookup(Mockito.anyString(), Mockito.anyInt());
    }

    @Test
    public void testGetTermWithOntologyNames(){
        int limit = 20;
        List<TermResource> termResources = new ArrayList<>();
        String ontologyName = "Kidney Cancer";
        Mockito.when(apiMockUp.lookup(ontologyName,limit)).thenReturn(termResources);
        autocompleteResource.getTerms(null, null, limit);
        verify(apiMockUp,times(1)).lookup(Mockito.anyString(), Mockito.anyInt());
    }

}
