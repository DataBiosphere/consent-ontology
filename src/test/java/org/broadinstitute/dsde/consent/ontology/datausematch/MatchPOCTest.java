package org.broadinstitute.dsde.consent.ontology.datausematch;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.broadinstitute.dsde.consent.ontology.datausematch.util.DataUseDecisions;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.resources.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.resources.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * See https://docs.google.com/document/d/1P70Gt5wCru0YzvJWNru9Nt4tCADhEsBWpKPp7qs_n2M/edit#
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
        DataUse consent = new DataUseBuilder().build();
        consent.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_162"); // Cancer
        DataUse purpose = new DataUseBuilder().build();
        String purposeId = "http://purl.obolibrary.org/obo/DOID_10155"; // Intestinal Cancer
        purpose.getDiseaseRestrictions().add(purposeId);

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
        assertTrue(matchPurposeAndDataset(purpose, consent));
    }

    @Test
    public void testDiseaseMatching_negative() throws Exception {
        DataUse consent = new DataUseBuilder().build();
        DataUse purpose = new DataUseBuilder().build();
        consent.getDiseaseRestrictions().add("http://purl.obolibrary.org/obo/DOID_10155"); // Intestinal Cancer
        String purposeId = "http://purl.obolibrary.org/obo/DOID_162"; //  Cancer
        purpose.getDiseaseRestrictions().add(purposeId);

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
        assertFalse(matchPurposeAndDataset(purpose, consent));
    }


    @Ignore // We don't have HMB RP logic in our table ... need to add that.
    @Test
    public void testHMB_positive() {
        DataUse consent = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
        assertTrue(matchPurposeAndDataset(purpose, consent));
    }

    @Ignore // We don't have HMB RP logic in our table ... need to add that.
    @Test
    public void testHMB_negative() {
        DataUse consent = new DataUseBuilder().setHmbResearch(true).build();
        DataUse purpose = new DataUseBuilder().setGeneralUse(true).build();
        assertFalse(matchPurposeAndDataset(purpose, consent));
    }

    @Test
    public void testNMDS_positive_case_1() {
        DataUse consent = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setMethodsResearch(true).build();
        assertTrue(matchPurposeAndDataset(purpose, consent));
    }

    @Test
    public void testNMDS_negative_case_1() {
        DataUse consent = new DataUseBuilder().setMethodsResearch(true).build();
        DataUse purpose = new DataUseBuilder().setMethodsResearch(true).build();
        assertFalse(matchPurposeAndDataset(purpose, consent));
    }


    // Matching Algorithm

    private boolean matchPurposeAndDataset(DataUse purpose, DataUse dataset) {
        // Calculating disease matches is expensive and used in multiple cases so we always calculate it
        boolean diseaseMatch = matchDiseases(purpose, dataset);
        boolean nmdsMatch = DataUseDecisions.matchNMDS(purpose, dataset, diseaseMatch);
        boolean controlMatch = DataUseDecisions.matchControlSet(purpose, dataset, diseaseMatch);

        log.info("diseaseMatch: " + diseaseMatch);
        log.info("nmdsMatch: " + nmdsMatch);
        log.info("controlMatch: " + controlMatch);

        return diseaseMatch &&
                nmdsMatch &&
                controlMatch;
    }

    // Helper methods

    private boolean matchDiseases(DataUse purpose, DataUse dataset) {
        if (purpose.getDiseaseRestrictions().isEmpty()) {
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
            return Optional.ofNullable(dataset.getGeneralUse()).orElse(false) ||
                    Optional.ofNullable(dataset.getHmbResearch()).orElse(false) ||
                    !matches.contains(false);
        }
    }

    private Map<String, List<String>> purposeDiseaseIdMap(List<String> diseaseRestrictions) {
        return diseaseRestrictions
                .stream()
                .collect(Collectors.toMap(r -> r, this::getPurposeTermIds, (a, b) -> b));
    }

    // Get a list of term ids that represent a disease term + all parent ids
    private List<String> getPurposeTermIds(String purposeDiseaseId) {
        List<String> purposeTermIdList = apiWrapper(purposeDiseaseId)
                .stream()
                .filter(Objects::nonNull)
                .flatMap(t -> t.parents.stream())
                .map(p -> p.id)
                .collect(Collectors.toList());
        purposeTermIdList.add(purposeDiseaseId);
        return purposeTermIdList;
    }

    private Collection<TermResource> apiWrapper(String termId) {
        try {
            return autocompleteService.lookupById(termId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
