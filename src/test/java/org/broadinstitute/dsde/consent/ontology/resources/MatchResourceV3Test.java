package org.broadinstitute.dsde.consent.ontology.resources;

import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV3;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilderV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class MatchResourceV3Test {

  @Mock
  private AutocompleteService autocompleteService;

  @Mock
  private final DataUseMatcherV3 dataUseMatcherV3 = new DataUseMatcherV3();

  private MatchResource resource;

  @BeforeEach
  public void setUp() throws Exception {
    openMocks(this);
    when(autocompleteService.lookupById(anyString())).thenReturn(Collections.emptyList());
    when(dataUseMatcherV3.matchPurposeAndDatasetV3(any(DataUseV3.class), any(DataUseV3.class))).thenReturn(
        MatchResult.from(MatchResultType.APPROVE, Collections.emptyList())
    );
  }

  private void initResource() {
    dataUseMatcherV3.setAutocompleteService(autocompleteService);
    resource = new MatchResource(dataUseMatcherV3);
  }

  @Test
  public void testOK() {
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    Response response = resource.matchDataUseV3(pair.toString());
    assertEquals(200, response.getStatus());
  }

  @Test
  public void testNullPurpose() {
    initResource();
    DataUseV3 purpose = null;
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    Response response = resource.matchDataUseV3(pair.toString());
    assertEquals(400, response.getStatus());
  }

  @Test
  public void testNullDataset() {
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 dataset = null;
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    Response response = resource.matchDataUseV3(pair.toString());
    assertEquals(400, response.getStatus());
  }

  @Test
  public void testInternalServerError() {
    doThrow(new RuntimeException("Something went wrong")).when(dataUseMatcherV3).matchPurposeAndDatasetV3(any(DataUseV3.class), any(DataUseV3.class));
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setDiseaseRestrictions(Collections.singletonList("http://purl.obolibrary.org/obo/DOID_162")).setMethodsResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    Response response = resource.matchDataUseV3(pair.toString());
    assertEquals(500, response.getStatus());
  }

}
