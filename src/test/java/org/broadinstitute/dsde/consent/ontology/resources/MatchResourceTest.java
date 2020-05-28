package org.broadinstitute.dsde.consent.ontology.resources;


import org.broadinstitute.dsde.consent.ontology.Utils;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MatchResourceTest {

    private final Logger log = Utils.getLogger(this.getClass());

    @Mock
    AutocompleteService autocompleteService;

    private MatchResource resource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(autocompleteService.lookupById(any())).thenReturn(Collections.emptyList());
        resource = new MatchResource();
        DataUseMatcher dataUseMatcher = new DataUseMatcher();
        dataUseMatcher.setAutocompleteService(autocompleteService);
        resource.setDataUseMatcher(dataUseMatcher);
    }

    @Test
    public void testOK() {
        DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        log.info(response.getEntity().toString());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testNullPurpose() {
        DataUse purpose = null;
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        log.info(response.getEntity().toString());
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testNullDataset() {
        DataUse purpose = new DataUseBuilder().setGeneralUse(true).build();
        DataUse dataset = null;
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        log.info(response.getEntity().toString());
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInternalServerError() throws Exception {
        Mockito.when(autocompleteService.lookupById(any())).thenThrow(new IOException("Something went wrong"));
        resource = new MatchResource();
        DataUse purpose = new DataUseBuilder().setDiseaseRestrictions(Collections.singletonList("http://purl.obolibrary.org/obo/DOID_162")).setMethodsResearch(true).build();
        DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        log.info(response.getEntity().toString());
        assertEquals(500, response.getStatus());
    }

}
