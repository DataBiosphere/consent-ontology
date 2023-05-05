package org.broadinstitute.dsde.consent.ontology.resources;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import java.util.List;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcher;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV3;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilderV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.broadinstitute.dsde.consent.ontology.service.AutocompleteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class MatchResourceTest {

  @Mock
  private AutocompleteService autocompleteService;

  @Mock
  private final DataUseMatcher dataUseMatcher = new DataUseMatcher();

  @Mock
  private final DataUseMatcherV3 dataUseMatcherV3 = new DataUseMatcherV3();

  private MatchResource resource;

  @BeforeEach
  public void setUp() throws Exception {
    openMocks(this);
    when(autocompleteService.lookupById(anyString())).thenReturn(Collections.emptyList());
    when(
        dataUseMatcher.matchPurposeAndDatasetV2(any(DataUse.class), any(DataUse.class))).thenReturn(
        new ImmutablePair<>(Boolean.TRUE, Collections.emptyList())
    );
    when(
        dataUseMatcherV3.matchPurposeAndDatasetV3(any(DataUseV3.class), any(DataUseV3.class))).thenReturn(
        new MatchResult(MatchResultType.APPROVE, Collections.emptyList())
    );

  }

  private void initResource() {
    dataUseMatcher.setAutocompleteService(autocompleteService);
    resource = new MatchResource(dataUseMatcher, dataUseMatcherV3);
  }

  @Test
  public void testOK() {
    initResource();
    DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
    DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
    DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
    try (Response response = resource.matchDataUse(pair)){
      assertEquals(200, response.getStatus());
    }
  }

  @Test
  public void testNullPurpose() {
    initResource();
    DataUse purpose = null;
    DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
    DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
    try (Response response = resource.matchDataUse(pair)){
      assertEquals(400, response.getStatus());
    }
  }

  @Test
  public void testNullDataset() {
    initResource();
    DataUse purpose = new DataUseBuilder().setGeneralUse(true).build();
    DataUse dataset = null;
    DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
    try( Response response = resource.matchDataUse(pair)){
      assertEquals(400, response.getStatus());
    }
  }

  @Test
  public void testInternalServerError() {
    doThrow(new RuntimeException("Something went wrong")).when(dataUseMatcher)
        .matchPurposeAndDatasetV2(any(DataUse.class), any(DataUse.class));
    initResource();
    DataUse purpose = new DataUseBuilder().setDiseaseRestrictions(
            Collections.singletonList("http://purl.obolibrary.org/obo/DOID_162"))
        .setMethodsResearch(true).build();
    DataUse dataset = new DataUseBuilder().setHmbResearch(true).build();
    DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
    try (Response response = resource.matchDataUse(pair)){
      assertEquals(500, response.getStatus());
    }
  }

  @Test
  public void testOKV3() {
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try( Response response = resource.matchDataUseV3(pair)){
      assertEquals(200, response.getStatus());
    }
  }

  @Test
  public void testNullPurposeV3() {
    initResource();
    DataUseV3 purpose = null;
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)){
      assertEquals(400, response.getStatus());
    }
  }

  @Test
  public void testNullDatasetV3() {
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 dataset = null;
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)){
      assertEquals(400, response.getStatus());
    }
  }

  @Test
  public void testInternalServerErrorV3() {
    doThrow(new RuntimeException("Something went wrong")).when(dataUseMatcherV3)
        .matchPurposeAndDatasetV3(any(DataUseV3.class), any(DataUseV3.class));
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setDiseaseRestrictions(
            Collections.singletonList("http://purl.obolibrary.org/obo/DOID_162"))
        .setMethodsResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      assertEquals(500, response.getStatus());
    }
  }

}
