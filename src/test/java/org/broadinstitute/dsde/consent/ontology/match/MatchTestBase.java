package org.broadinstitute.dsde.consent.ontology.match;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.broadinstitute.dsde.consent.ontology.truthtable.TruthTableTests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchControlSet;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNAGR;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchNMDS;
import static org.broadinstitute.dsde.consent.ontology.datause.DataUseMatchCases.matchPOA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by davidan on 5/3/17.
 */
abstract class MatchTestBase extends TruthTableTests {

    private static final Logger log = Logger.getLogger(MatchTestBase.class);
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
        MockitoAnnotations.initMocks(this);
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
        boolean algorithmMatch = matcher.matchPurposeAndDataset(purpose, consent);
        String testMessage = testName + "; expected result: " + expectedMatchResult;
        assertEquals(testMessage, expectedMatchResult, algorithmMatch);
    }

}
