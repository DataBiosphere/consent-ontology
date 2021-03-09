package org.broadinstitute.dsde.consent.ontology.resources;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.service.StoreOntologyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MatchResourceTest {

    @Mock
    private AutocompleteService autocompleteService;

    @Mock
    private StoreOntologyService storeOntologyService;

    @Mock
    private final DataUseMatcher dataUseMatcher = new DataUseMatcher();

    private MatchResource resource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(autocompleteService.lookupById(any())).thenReturn(Collections.emptyList());
        when(dataUseMatcher.matchPurposeAndDatasetV2(any(), any())).thenReturn(
            new ImmutablePair<>(Boolean.TRUE, Collections.emptyList())
        );
    }

    private void initResource() {
        dataUseMatcher.setAutocompleteService(autocompleteService);
        resource = new MatchResource(dataUseMatcher, storeOntologyService);
    }

    @Test
    public void testOK() {
        initResource();
        DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testNullPurpose() {
        initResource();
        DataUse purpose = null;
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNullDataset() {
        initResource();
        DataUse purpose = new DataUseBuilder().setGeneralUse(true).build();
        DataUse dataset = null;
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInternalServerError() {
        doThrow(new RuntimeException("Something went wrong")).when(dataUseMatcher).matchPurposeAndDatasetV2(any(), any());
        initResource();
        DataUse purpose = new DataUseBuilder().setDiseaseRestrictions(Collections.singletonList("http://purl.obolibrary.org/obo/DOID_162")).setMethodsResearch(true).build();
        DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        assertEquals(500, response.getStatus());
    }

}
