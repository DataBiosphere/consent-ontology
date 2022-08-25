package org.broadinstitute.dsde.consent.ontology.datause;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * See https://docs.google.com/document/d/1P70Gt5wCru0YzvJWNru9Nt4tCADhEsBWpKPp7qs_n2M
 * for minimum number of use cases that need to be implemented.
 */
public class DataUseMatcherTest {

    private static final String cancerNode = "http://purl.obolibrary.org/obo/DOID_162";
    private static final String intestinalCancerNode = "http://purl.obolibrary.org/obo/DOID_10155";

    @Mock
    private AutocompleteService autocompleteService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        try {
            when(autocompleteService.lookupById(any())).thenReturn(Collections.emptyList());
        } catch (Exception e) {
            //
        }
    }

    @Test
    public void testDiseaseMatching_positive() throws Exception {
        DataUse dataset = new DataUseBuilder()
                .setDiseaseRestrictions(Collections.singletonList(cancerNode))
                .build();
        DataUse purpose = new DataUseBuilder()
                .setDiseaseRestrictions(Collections.singletonList(intestinalCancerNode))
                .build();
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

        resource.setId(intestinalCancerNode);
        resource.setParents(new ArrayList<>());
        resource.getParents().add(parent1);
        resource.getParents().add(parent2);
        resource.getParents().add(parent3);
        resource.getParents().add(parent4);
        resource.getParents().add(parent5);

        termResources.add(resource);

        when(autocompleteService.lookupById(intestinalCancerNode)).thenReturn(termResources);
        assertPositive(purpose, dataset);
    }

    @Test
    public void testDiseaseMatching_negative() throws Exception {
        DataUse dataset = new DataUseBuilder()
                .setDiseaseRestrictions(Collections.singletonList(intestinalCancerNode))
                .build();
        DataUse purpose = new DataUseBuilder()
                .setDiseaseRestrictions(Collections.singletonList(cancerNode))
                .build();

        // Build a mock response of term parents based on what is returned when searching on DOID_162
        List<TermResource> termResources = new ArrayList<>();
        TermResource resource = new TermResource();
        TermParent parent1 = new TermParent();
        parent1.setId("http://purl.obolibrary.org/obo/DOID_14566");
        TermParent parent2 = new TermParent();
        parent2.setId("http://purl.obolibrary.org/obo/DOID_4");

        resource.setId(cancerNode);
        resource.setParents(new ArrayList<>());
        resource.getParents().add(parent1);
        resource.getParents().add(parent2);

        termResources.add(resource);

        when(autocompleteService.lookupById(cancerNode)).thenReturn(termResources);
        assertNegative(purpose, dataset);
    }

    @Test
    public void testDiseaseMatching_noPurposeDiseases() {
        DataUse dataset = new DataUseBuilder()
                .setDiseaseRestrictions(Collections.singletonList(cancerNode))
                .build();
        DataUse purpose = new DataUseBuilder().build();
        assertNegative(purpose, dataset);
    }

    @Test
    public void testHMB_positive() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testHMB_negative() {
        DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
        DataUse purpose = new DataUseBuilder().setGeneralUse(true).build();
        assertNegative(purpose, dataset);
    }

    @Test
    public void testNMDS_positive_case_1() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setMethodsResearch(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testNMDS_negative_case_1() {
        DataUse dataset = new DataUseBuilder().setMethodsResearch(true).build();
        DataUse purpose = new DataUseBuilder()
                .setDiseaseRestrictions(Collections.singletonList(cancerNode))
                .setMethodsResearch(true).build();
        assertNegative(purpose, dataset);
    }

    // TODO: This is confusing. In the context of a consented dataset, this means no aggregate research
    // In the context of a research purpose, this means yes to aggregate research
    @Test
    public void testNAGR_positive() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).setAggregateResearch("No").build();
        DataUse purpose = new DataUseBuilder().setAggregateResearch("Yes").build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testNAGR_negative() {
        DataUse dataset = new DataUseBuilder().setAggregateResearch("No").build(); // Not GRU or HMB
        DataUse purpose = new DataUseBuilder().setAggregateResearch("Yes").build();
        assertNegative(purpose, dataset);
    }

    @Test
    public void testPOA_positive() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setPopulationOriginsAncestry(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testPOA_negative() {
        DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
        DataUse purpose = new DataUseBuilder().setPopulationOriginsAncestry(true).build();
        assertNegative(purpose, dataset);
    }

    @Test
    public void testCommercial_positive_1() {
        DataUse dataset = new DataUseBuilder().setCommercialUse(true).build();
        DataUse purpose = new DataUseBuilder().setCommercialUse(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testCommercial_positive_2() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setCommercialUse(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testCommercial_negative() {
        DataUse dataset = new DataUseBuilder().setCommercialUse(false).build();
        DataUse purpose = new DataUseBuilder().setCommercialUse(true).build();
        assertNegative(purpose, dataset);
    }

    @Test
    public void testPediatric_positive_1() {
        DataUse dataset = new DataUseBuilder().setPediatric(true).build();
        DataUse purpose = new DataUseBuilder().setPediatric(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testPediatric_positive_2() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setPediatric(true).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testPediatric_positive_3() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setPediatric(false).build();
        assertPositive(purpose, dataset);
    }

    @Test
    public void testPediatric_negative_1() {
        DataUse dataset = new DataUseBuilder().setPediatric(true).build();
        DataUse purpose = new DataUseBuilder().setPediatric(false).build();
        assertNegative(purpose, dataset);
    }

    @Test
    public void testGenderMatch_positive_1() {
        DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
        DataUse purpose = new DataUseBuilder().setGender("Male").build();
        assertPositive(purpose, dataset);

        purpose.setGender("Female");
        assertPositive(purpose, dataset);
    }

    @Test
    public void testGenderMatch_positive_2() {
        DataUse dataset = new DataUseBuilder().setGender("N/A").build();
        DataUse purpose = new DataUseBuilder().setGender("Male").build();
        assertPositive(purpose, dataset);

        purpose.setGender("Female");
        assertPositive(purpose, dataset);
    }

    @Test
    public void testGenderMatch_negative_1() {
        DataUse dataset = new DataUseBuilder().setGender("Female").build();
        DataUse purpose = new DataUseBuilder().setGender("Male").build();
        assertNegative(purpose, dataset);

        dataset.setGender("Male");
        purpose.setGender("Female");
        assertNegative(purpose, dataset);
    }

    private void assertPositive(DataUse purpose, DataUse dataset) {
        ImmutablePair<Boolean, List<String>> match = matchPurposeAndDataset(purpose, dataset);
        assertTrue(match.getLeft());
        assertTrue(match.getRight().isEmpty());
    }

    private void assertNegative(DataUse purpose, DataUse dataset) {
        ImmutablePair<Boolean, List<String>> match = matchPurposeAndDataset(purpose, dataset);
        assertFalse(match.getLeft());
        assertFalse(match.getRight().isEmpty());
    }

    private ImmutablePair<Boolean, List<String>> matchPurposeAndDataset(DataUse purpose, DataUse dataset) {
        DataUseMatcher matcher = new DataUseMatcher();
        matcher.setAutocompleteService(autocompleteService);
        return matcher.matchPurposeAndDatasetV2(purpose, dataset);
    }

}
