package org.broadinstitute.dsde.consent.ontology.resources;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class MatchResourceTest {

    @Mock
    private AutocompleteService autocompleteService;

    @Mock
    private final DataUseMatcher dataUseMatcher = new DataUseMatcher();

    private MatchResource resource;

    @BeforeEach
    public void setUp() throws Exception {
        openMocks(this);
        when(autocompleteService.lookupById(anyString())).thenReturn(Collections.emptyList());
        when(dataUseMatcher.matchPurposeAndDatasetV2(any(DataUse.class), any(DataUse.class))).thenReturn(
            new ImmutablePair<>(Boolean.TRUE, Collections.emptyList())
        );
    }

    private void initResource() {
        dataUseMatcher.setAutocompleteService(autocompleteService);
        resource = new MatchResource(dataUseMatcher);
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
        doThrow(new RuntimeException("Something went wrong")).when(dataUseMatcher).matchPurposeAndDatasetV2(any(DataUse.class), any(DataUse.class));
        initResource();
        DataUse purpose = new DataUseBuilder().setDiseaseRestrictions(Collections.singletonList("http://purl.obolibrary.org/obo/DOID_162")).setMethodsResearch(true).build();
        DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
        DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
        Response response = resource.matchDataUse(pair);
        assertEquals(500, response.getStatus());
    }

}
