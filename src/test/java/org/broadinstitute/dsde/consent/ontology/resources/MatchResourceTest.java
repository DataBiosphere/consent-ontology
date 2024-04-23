package org.broadinstitute.dsde.consent.ontology.resources;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV3;
import org.broadinstitute.dsde.consent.ontology.datause.DataUseMatcherV4;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResult;
import org.broadinstitute.dsde.consent.ontology.datause.MatchResultType;
import org.broadinstitute.dsde.consent.ontology.datause.MatchV3ResponseEntity;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilder;
import org.broadinstitute.dsde.consent.ontology.model.DataUseBuilderV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPair;
import org.broadinstitute.dsde.consent.ontology.model.DataUseMatchPairV3;
import org.broadinstitute.dsde.consent.ontology.model.DataUseV3;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchResourceTest {

  @Mock
  private final DataUseMatcherV3 dataUseMatcherV3 = new DataUseMatcherV3();

  @Mock
  private final DataUseMatcherV4 dataUseMatcherV4 = new DataUseMatcherV4();

  private MatchResource resource;

  private void initResource() {
    resource = new MatchResource(dataUseMatcherV3, dataUseMatcherV4);
  }

  @Test
  void testGone() {
    initResource();
    DataUse purpose = new DataUseBuilder().setHmbResearch(true).build();
    DataUse dataset = new DataUseBuilder().setGeneralUse(true).build();
    DataUseMatchPair pair = new DataUseMatchPair(purpose, dataset);
    try (Response response = resource.matchDataUse(pair)) {
      assertEquals(410, response.getStatus());
    }
  }

  @Test
  void testOKV3() {
    when(
        dataUseMatcherV3.matchPurposeAndDatasetV3(any(DataUseV3.class),
            any(DataUseV3.class))).thenReturn(
        new MatchResult(MatchResultType.APPROVE, Collections.emptyList())
    );
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      assertEquals(200, response.getStatus());
    }
  }

  @Test
  void testOKV3ResponseApprove() {
    when(
        dataUseMatcherV3.matchPurposeAndDatasetV3(any(DataUseV3.class),
            any(DataUseV3.class))).thenReturn(
        new MatchResult(MatchResultType.APPROVE, Collections.emptyList())
    );
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setHmbResearch(true).build();
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      String stringEntity = response.getEntity().toString();
      MatchV3ResponseEntity entity = new Gson().fromJson(stringEntity, MatchV3ResponseEntity.class);
      assertEquals(MatchResultType.APPROVE, entity.getResult());
      assertEquals(pair, entity.getMatchPair());
      assertEquals(Collections.emptyList(), entity.getRationale());
    }
  }

  @Test
  void testOKV3ResponseDeny() {
    initResource();
    MatchResultType deny = MatchResultType.DENY;
    when(
        dataUseMatcherV3.matchPurposeAndDatasetV3(any(DataUseV3.class),
            any(DataUseV3.class))).thenReturn(
        new MatchResult(deny, Collections.emptyList())
    );
    DataUseV3 purpose = new DataUseBuilderV3().setSecondaryOther("true").build();
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      String stringEntity = response.getEntity().toString();
      MatchV3ResponseEntity entity = new Gson().fromJson(stringEntity, MatchV3ResponseEntity.class);
      assertEquals(deny, entity.getResult());
      assertEquals(pair, entity.getMatchPair());
      assertEquals(Collections.emptyList(), entity.getRationale());
    }
  }

  @Test
  void testOKV3ResponseAbstain() {
    initResource();
    MatchResultType abstain = MatchResultType.ABSTAIN;
    when(
        dataUseMatcherV3.matchPurposeAndDatasetV3(any(DataUseV3.class),
            any(DataUseV3.class))).thenReturn(
        new MatchResult(abstain, Collections.emptyList())
    );
    DataUseV3 purpose = new DataUseBuilderV3().setSecondaryOther("true").build();
    DataUseV3 dataset = new DataUseBuilderV3().setPopulationOriginsAncestry(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      String stringEntity = response.getEntity().toString();
      MatchV3ResponseEntity entity = new Gson().fromJson(stringEntity, MatchV3ResponseEntity.class);
      assertEquals(abstain, entity.getResult());
      assertEquals(pair, entity.getMatchPair());
      assertEquals(Collections.emptyList(), entity.getRationale());
    }
  }

  @Test
  void testNullPurposeV3() {
    initResource();
    DataUseV3 purpose = null;
    DataUseV3 dataset = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      assertEquals(400, response.getStatus());
    }
  }

  @Test
  void testNullDatasetV3() {
    initResource();
    DataUseV3 purpose = new DataUseBuilderV3().setGeneralUse(true).build();
    DataUseV3 dataset = null;
    DataUseMatchPairV3 pair = new DataUseMatchPairV3(purpose, dataset);
    try (Response response = resource.matchDataUseV3(pair)) {
      assertEquals(400, response.getStatus());
    }
  }

  @Test
  void testInternalServerErrorV3() {
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
