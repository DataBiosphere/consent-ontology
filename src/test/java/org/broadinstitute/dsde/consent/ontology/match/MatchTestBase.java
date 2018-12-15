package org.broadinstitute.dsde.consent.ontology.match;

import org.apache.log4j.Logger;
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

import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchControlSet;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchDiseases;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchNAGR;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchNMDS;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchPOA;
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
    public void parameterizedTest() {
        matchPurposeAndDataset(purpose, consent, expectedMatchResult);
    }

    // Matching Algorithm.
    // TODO: Extract to a service

    private void matchPurposeAndDataset(DataUse purpose, DataUse dataset, boolean expected) {
        Map<String, List<String>> purposeDiseaseIdMap = purposeDiseaseIdMap(purpose.getDiseaseRestrictions());

        boolean diseaseMatch = matchDiseases(purpose, dataset, purposeDiseaseIdMap);
        boolean hmbMatch = matchHMB(purpose, dataset, diseaseMatch);
        boolean nmdsMatch = matchNMDS(purpose, dataset, diseaseMatch);
        boolean controlMatch = matchControlSet(purpose, dataset, diseaseMatch);
        boolean nagrMatch = matchNAGR(purpose, dataset);
        boolean poaMatch = matchPOA(purpose, dataset);

        log.info("hmbMatch: " + hmbMatch);
        log.info("diseaseMatch: " + diseaseMatch);
        log.info("nmdsMatch: " + nmdsMatch);
        log.info("controlMatch: " + controlMatch);
        log.info("nagrMatch: " + nagrMatch);
        log.info("poaMatch: " + poaMatch);

        boolean algorithmMatch = diseaseMatch &&
                nmdsMatch &&
                controlMatch &&
                nagrMatch &&
                poaMatch;
        String testMessage = testName + "; expected result: " + expected;
        assertEquals(testMessage, expected, algorithmMatch);
    }

    // Helper methods

    // Get a map of disease term to list of parent term ids (which also includes disease term id)
    private Map<String, List<String>> purposeDiseaseIdMap(List<String> diseaseRestrictions) {
        return diseaseRestrictions
                .stream()
                .collect(Collectors.toMap(r -> r, this::getParentTermIds, (a, b) -> b));
    }

    // Get a list of term ids that represent a disease term + all parent ids
    private List<String> getParentTermIds(String purposeDiseaseId) {
        List<String> purposeTermIdList = apiWrapper(purposeDiseaseId)
                .stream()
                .filter(Objects::nonNull)
                .flatMap(t -> t.parents.stream())
                .map(p -> p.id)
                .collect(Collectors.toList());
        purposeTermIdList.add(purposeDiseaseId);
        return purposeTermIdList;
    }

    // Silly wrapper around the lookup api call to swallow errors
    private Collection<TermResource> apiWrapper(String termId) {
        try {
            return autocompleteService.lookupById(termId);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

}
