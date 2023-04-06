package org.broadinstitute.dsde.consent.ontology.datause;

import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilderV3;
import org.broadinstitute.dsde.consent.ontology.model.TermParent;
import org.broadinstitute.dsde.consent.ontology.model.TermResource;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Abstain;
import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Approve;
import static org.broadinstitute.dsde.consent.ontology.datause.MatchResultType.Deny;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class DataUseMatcherV3Test {

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
  public void testDiseaseMatching_negative() throws Exception {
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
  public void testDiseaseMatching_noPurposeDiseases() {
    DataUseV3 dataset = new DataUseBuilderV3()
        .setDiseaseRestrictions(Collections.singletonList(cancerNode))
        .build();
    DataUseV3 purpose = new DataUseBuilderV3().setDiseaseRestrictions(List.of()).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testMDS_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testMDS_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testMDS_negative_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setMethodsResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3()
        .setDiseaseRestrictions(Collections.singletonList(cancerNode))
        .setMethodsResearch(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testMDS_negative_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setMethodsResearch(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testPOA_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testPOA_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testPOA_negative() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testCommercial_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setCommercialUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testCommercial_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testCommercial_positive_case_3() {
    DataUseV3 dataset = new DataUseBuilderV3().setNonProfitUse(null).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testCommercial_negative_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setNonProfitUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testCommercial_negative_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setCommercialUse(false).build();
    DataUseV3 purpose = new DataUseBuilderV3().setCommercialUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testHMB_positive_case_1() {
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testHMB_positive_case_2() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testHMB_negative() {
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 purpose = new DataUseBuilderV3().setGeneralUse(true).build();
    assertDeny(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_case_1() {
    DataUseV3 purpose = new DataUseBuilderV3().setCollaboratorRequired(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertAbstain(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_case_2() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    assertApprove(purpose, dataset);
  }

  @Test
  public void testAbstainDecision_case_3() {
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(false).build();
    assertDeny(purpose, dataset);
  }

  private void assertApprove(DataUseV3 purpose, DataUseV3 dataset) {
    MatchResult match = matchPurposeAndDataset(purpose, dataset);
    assertTrue(Approve(match.getMatchResultType()));
    assertTrue(match.getMessage().isEmpty());
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