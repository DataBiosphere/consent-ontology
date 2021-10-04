package org.broadinstitute.dsde.consent.ontology.match;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.IOException;
import java.util.Collections;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.truthtable.TruthTableTests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by davidan on 5/3/17.
 */
abstract class MatchTestBase extends TruthTableTests {

    private DataUse purpose;
    private DataUse consent;
    private Boolean expectedMatchResult;
    private String testName;

    MatchTestBase(DataUse purpose, DataUse consent, Boolean expectedMatchResult, String testName) {
        this.purpose = purpose;
        this.consent = consent;
        this.expectedMatchResult = expectedMatchResult;
        this.testName = testName;
    }

    @Mock
    AutocompleteService autocompleteService;

    @Before
    public void setUp() {
        openMocks(this);
        try {
            when(autocompleteService.lookupById(any())).thenReturn(Collections.emptyList());
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void parameterizedTest() throws IOException {
        DataUseMatcher matcher = new DataUseMatcher();
        matcher.setAutocompleteService(autocompleteService);
        boolean algorithmMatch = matcher.matchPurposeAndDatasetV2(purpose, consent).getLeft();
        String testMessage = testName + "; expected result: " + expectedMatchResult;
        assertEquals(testMessage, expectedMatchResult, algorithmMatch);
    }

}
