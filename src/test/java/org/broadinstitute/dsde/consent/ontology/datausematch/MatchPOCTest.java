package org.broadinstitute.dsde.consent.ontology.datausematch;

import org.apache.log4j.Logger;
import org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchControlSet;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchHMB;
import static org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions.matchNMDS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * See https://docs.google.com/document/d/1P70Gt5wCru0YzvJWNru9Nt4tCADhEsBWpKPp7qs_n2M
 * for minimum number of use cases that need to be implemented.
 *
 */
public class MatchPOCTest {

    private static final Logger log = Logger.getLogger(MatchPOCTest.class);

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
    public void testDiseaseMatching_positive() throws Exception {
        DataUse dataset = new DataUseBuilder().build();
        dataset.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_162"); // Cancer
        DataUse purpose = new DataUseBuilder().build();
        String purposeId = "http://purl.obolibrary.org/obo/DOID_10155"; // Intestinal Cancer
        purpose.getDiseaseRestrictions().add(purposeId);

        // Build a mock response of term parents based on what is returned when searching on DOID_10155
        List<TermResource> termResources = new ArrayList<>();
        TermResource resource = new TermResource();
        TermParent parent1 = new TermParent();
        parent1.setId("http://purl.obolibrary.org/obo/DOID_3119");
        TermParent parent2 = new TermParent();
        parent2.setId("http://purl.obolibrary.org/obo/DOID_0050686");
        TermParent parent3 = new TermParent();
        parent3.setId("http://purl.obolibrary.org/obo/DOID_162");
        TermParent parent4 = new TermParent();
        parent4.setId("http://purl.obolibrary.org/obo/DOID_14566");
        TermParent parent5 = new TermParent();
        parent5.setId("http://purl.obolibrary.org/obo/DOID_4");

        resource.setId(purposeId);
        resource.setParents(new ArrayList<>());
        resource.getParents().add(parent1);
        resource.getParents().add(parent2);
        resource.getParents().add(parent3);
        resource.getParents().add(parent4);
        resource.getParents().add(parent5);

        termResources.add(resource);

        when(autocompleteService.lookupById(purposeId)).thenReturn(termResources);
        assertTrue(matchPurposeAndDataset(purpose, dataset));
    }

    @Test
    public void testDiseaseMatching_negative() throws Exception {
        DataUse dataset = new DataUseBuilder().build();
        DataUse purpose = new DataUseBuilder().build();
        dataset.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_10155"); // Intestinal Cancer
        String purposeId = "http://purl.obolibrary.org/obo/DOID_162"; //  Cancer
        purpose.getDiseaseRestrictions().add(purposeId);

        // Build a mock response of term parents based on what is returned when searching on DOID_162
        List<TermResource> termResources = new ArrayList<>();
        TermResource resource = new TermResource();
        TermParent parent1 = new TermParent();
        parent1.setId("http://purl.obolibrary.org/obo/DOID_14566");
        TermParent parent2 = new TermParent();
        parent2.setId("http://purl.obolibrary.org/obo/DOID_4");

        resource.setId(purposeId);
        resource.setParents(new ArrayList<>());
        resource.getParents().add(parent1);
        resource.getParents().add(parent2);

        termResources.add(resource);

        when(autocompleteService.lookupById(purposeId)).thenReturn(termResources);
        assertFalse(matchPurposeAndDataset(purpose, dataset));
    }

    @Test
    public void testHMB_positive() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
        assertTrue(matchPurposeAndDataset(purpose, dataset));
    }

    @Test
    public void testHMB_negative() {
        DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
        DataUse purpose = new DataUseBuilder().setGeneralUse(true).build();
        assertFalse(matchPurposeAndDataset(purpose, dataset));
    }

    @Test
    public void testNMDS_positive_case_1() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setMethodsResearch(true).build();
        assertTrue(matchPurposeAndDataset(purpose, dataset));
    }

    @Test
    public void testNMDS_negative_case_1() {
        // TODO: This is confusing. In the context of a consented dataset, this means no methods research
        DataUse dataset = new DataUseBuilder().setMethodsResearch(true).build();
        // TODO: This is confusing. In the context of a research purpose, this means yes to methods research
        DataUse purpose = new DataUseBuilder().setMethodsResearch(true).build();
        assertFalse(matchPurposeAndDataset(purpose, dataset));
    }


    // Matching Algorithm

    private boolean matchPurposeAndDataset(DataUse purpose, DataUse dataset) {
        // Calculating disease matches is expensive and used in multiple cases so we always calculate it
        boolean hmbMatch = matchHMB(purpose, dataset);
        boolean diseaseMatch = matchDiseases(purpose, dataset);
        boolean nmdsMatch = matchNMDS(purpose, dataset, diseaseMatch);
        boolean controlMatch = matchControlSet(purpose, dataset, diseaseMatch);

        log.info("hmbMatch: " + hmbMatch);
        log.info("diseaseMatch: " + diseaseMatch);
        log.info("nmdsMatch: " + nmdsMatch);
        log.info("controlMatch: " + controlMatch);

        return hmbMatch &&
                diseaseMatch &&
                nmdsMatch &&
                controlMatch;
    }

    /**
     * RP: Disease Focused Research
     * Datasets:
     *      Any dataset with GRU=true
     *      Any dataset with HMB=true
     *      Any dataset tagged to this disease exactly
     *      Any dataset tagged to a DOID ontology Parent of disease X
     *
     */
    private boolean matchDiseases(DataUse purpose, DataUse dataset) {
        if (purpose.getDiseaseRestrictions().isEmpty()) {
            return true;
        }
        if (DataUseDecisions.getNullable(dataset.getGeneralUse())) {
            return true;
        }
        if (DataUseDecisions.getNullable(dataset.getHmbResearch())) {
            return true;
        } else {
            Map<String, List<String>> purposeDiseaseIdMap = purposeDiseaseIdMap(purpose.getDiseaseRestrictions());
            // We want all purpose disease IDs to be a subclass of any dataset disease ID
            Set<Boolean> matches = purposeDiseaseIdMap
                    .values()
                    .stream()
                    .map(idList -> idList
                            .stream()
                            .anyMatch(dataset.getDiseaseRestrictions()::contains))
                    .collect(Collectors.toSet());
            return !matches.contains(false);
        }
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
