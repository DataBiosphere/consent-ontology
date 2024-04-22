package org.broadinstitute.dsde.consent.ontology.datause;

import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Abstain;
import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Approve;
import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Deny;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilderV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataUseMatcherV3Test {

  private static final String cancerNode = "http://purl.obolibrary.org/obo/DOID_162";
  private static final String intestinalCancerNode = "http://purl.obolibrary.org/obo/DOID_10155";

  @Mock
  private AutocompleteService autocompleteService;

  @Test
  void testDiseaseMatching_positive() throws Exception {
    DataUseV3 dataset = new DataUseBuilderV3()
        .setDiseaseRestrictions(Collections.singletonList(cancerNode))
        .build();
    DataUseV3 purpose = new DataUseBuilderV3()
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
    assertApprove(purpose, dataset);
  }

  @Test
  void testDiseaseMatching_negative() throws Exception {
    DataUseV3 dataset = new DataUseBuilderV3()
        .setDiseaseRestrictions(Collections.singletonList(intestinalCancerNode))
        .build();
    DataUseV3 purpose = new DataUseBuilderV3()
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
    assertDeny(purpose, dataset);
  }

  @Test
  void testHMB_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testHMB_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testHMB_negative_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setGeneralUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testHMB_negative_case_2() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setDiseaseRestrictions(
        Collections.singletonList(cancerNode)).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testHMB_negative_case_3() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testPOA_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testPOA_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testPOA_negative_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testPOA_negative_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3()
        .setDiseaseRestrictions(Collections.singletonList(cancerNode))
        .build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testMDS_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testMDS_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testMDS_positive_case_3() {
    DataUseV3 dataset = new DataUseBuilderV3().setDiseaseRestrictions(
        Collections.singletonList(cancerNode)).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testMDS_positive_case_4() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testCommercial_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setCommercialUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testCommercial_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  void testCommercial_negative_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setNonProfitUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testCommercial_negative_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setCommercialUse(false).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  void testAbstainDecision_COL() {
    DataUseV3 purpose = new DataUseBuilderV3().setCollaboratorRequired(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_Other() {
    DataUseV3 purpose = new DataUseBuilderV3().setOther("other").build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_SecondaryOther() {
    DataUseV3 purpose = new DataUseBuilderV3().setSecondaryOther("secondary other").build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_ethicsApprovalRequired() {
    DataUseV3 purpose = new DataUseBuilderV3().setEthicsApprovalRequired(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_controls() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setControls(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_population() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setPopulation(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_gender() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setGender("M").build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_pediatric() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setPediatric(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_vulnerablePopulations() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setVulnerablePopulations(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_illegalBehavior() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setIllegalBehavior(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_sexualDiseases() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setSexualDiseases(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_psychologicalTraits() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setPsychologicalTraits(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_notHealth() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setNotHealth(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  void testAbstainDecision_stigmatizedDiseases() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).setStigmatizeDiseases(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  private void assertApprove(DataUseV3 purpose, DataUseV3 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Approve(match.getMatchResultType()));
  }

  private void assertDeny(DataUseV3 purpose, DataUseV3 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Deny(match.getMatchResultType()));
    assertFalse(match.getMessage().isEmpty());
  }

  private void assertAbstain(DataUseV3 purpose, DataUseV3 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Abstain(match.getMatchResultType()));
    assertFalse(match.getMessage().isEmpty());
  }

  private MatchResult matchPurposeAndDataset(DataUseV3 purpose, DataUseV3 dataset) {
    DataUseMatcherV3 matcher = new DataUseMatcherV3();
    matcher.setAutocompleteService(autocompleteService);
    return matcher.matchPurposeAndDatasetV3(purpose, dataset);
  }
}
