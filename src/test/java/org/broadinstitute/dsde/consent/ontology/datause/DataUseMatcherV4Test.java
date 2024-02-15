package org.broadinstitute.dsde.consent.ontology.datause;

import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Abstain;
import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Approve;
import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Deny;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilderV4;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV4;
import org.broadinstitute.dsde.consent.ontology.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class DataUseMatcherV4Test {

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
    DataUseV4 dataset = new DataUseBuilderV4()
        .setDiseaseRestrictions(Collections.singletonList(cancerNode))
        .build();
    DataUseV4 purpose = new DataUseBuilderV4()
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
  public void testDiseaseMatching_negative() throws Exception {
    DataUseV4 dataset = new DataUseBuilderV4()
        .setDiseaseRestrictions(Collections.singletonList(intestinalCancerNode))
        .build();
    DataUseV4 purpose = new DataUseBuilderV4()
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
  public void testHMB_positive_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setGeneralUse(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testHMB_positive_case_2() {
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testHMB_negative_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setGeneralUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testHMB_negative_case_2() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setDiseaseRestrictions(
        Collections.singletonList(cancerNode)).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testHMB_negative_case_3() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testPOA_positive_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setGeneralUse(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testPOA_positive_case_2() {
    DataUseV4 dataset = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testPOA_negative_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testPOA_negative_case_2() {
    DataUseV4 dataset = new DataUseBuilderV4()
        .setDiseaseRestrictions(Collections.singletonList(cancerNode))
        .build();
    DataUseV4 purpose = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testMDS_positive_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setGeneralUse(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testMDS_positive_case_2() {
    DataUseV4 dataset = new DataUseBuilderV4().setPopulationOriginsAncestry(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testMDS_positive_case_3() {
    DataUseV4 dataset = new DataUseBuilderV4().setDiseaseRestrictions(
        Collections.singletonList(cancerNode)).build();
    DataUseV4 purpose = new DataUseBuilderV4().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testMDS_positive_case_4() {
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testNPU_positive_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setNonProfitUse(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setNonProfitUse(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testNPU_positive_case_2() {
    DataUseV4 dataset = new DataUseBuilderV4().setNonProfitUse(false).build();
    DataUseV4 purpose = new DataUseBuilderV4().setNonProfitUse(false).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testNPU_negative_case_1() {
    DataUseV4 dataset = new DataUseBuilderV4().setNonProfitUse(true).build();
    DataUseV4 purpose = new DataUseBuilderV4().setNonProfitUse(false).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testNPU_negative_case_2() {
    DataUseV4 dataset = new DataUseBuilderV4().setNonProfitUse(false).build();
    DataUseV4 purpose = new DataUseBuilderV4().setNonProfitUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_COL() {
    DataUseV4 purpose = new DataUseBuilderV4().setCollaboratorRequired(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_Other() {
    DataUseV4 purpose = new DataUseBuilderV4().setOther("other").build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_SecondaryOther() {
    DataUseV4 purpose = new DataUseBuilderV4().setSecondaryOther("secondary other").build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_ethicsApprovalRequired() {
    DataUseV4 purpose = new DataUseBuilderV4().setEthicsApprovalRequired(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_controls() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setControls(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_population() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setPopulation(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_gender() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setGender("M").build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_pediatric() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setPediatric(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_vulnerablePopulations() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setVulnerablePopulations(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_illegalBehavior() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setIllegalBehavior(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_sexualDiseases() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setSexualDiseases(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_psychologicalTraits() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setPsychologicalTraits(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_notHealth() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setNotHealth(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_stigmatizedDiseases() {
    DataUseV4 purpose = new DataUseBuilderV4().setHmbResearch(true).setStigmatizeDiseases(true).build();
    DataUseV4 dataset = new DataUseBuilderV4().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  private void assertApprove(DataUseV4 purpose, DataUseV4 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Approve(match.getMatchResultType()));
  }

  private void assertDeny(DataUseV4 purpose, DataUseV4 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Deny(match.getMatchResultType()));
    assertFalse(match.getMessage().isEmpty());
  }

  private void assertAbstain(DataUseV4 purpose, DataUseV4 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Abstain(match.getMatchResultType()));
    assertFalse(match.getMessage().isEmpty());
  }

  private MatchResult matchPurposeAndDataset(DataUseV4 purpose, DataUseV4 dataset) {
    DataUseMatcherV4 matcher = new DataUseMatcherV4();
    matcher.setAutocompleteService(autocompleteService);
    return matcher.matchPurposeAndDatasetV4(purpose, dataset);
  }
}
