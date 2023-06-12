package org.broadinstitute.dsde.consent.ontology.resources.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import java.util.Set;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.parboiled.common.FileUtils;

public class DataUseTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static JsonSchema JSON_SCHEMA;

  @BeforeAll
  public static void setUp() throws Exception {
    String DU_CONTENT = FileUtils.readAllTextFromResource("data-use.json");
    JsonSchemaFactory LOADER = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
    JSON_SCHEMA = LOADER.getSchema(MAPPER.readTree(DU_CONTENT));
  }

  private void assertInvalidJson(String snippet) throws Exception {
    Set<ValidationMessage> messages = JSON_SCHEMA.validate(MAPPER.readTree(snippet));
    assertFalse(messages.isEmpty());
  }

  private void assertValidJson(String snippet) throws Exception {
    Set<ValidationMessage> messages = JSON_SCHEMA.validate(MAPPER.readTree(snippet));
    assertTrue(messages.isEmpty());
  }

  /*
   *  Tests that look at individual fields in DataUse
   */

  @Test
  public void generalUse() throws Exception {
    String json = "{ \"generalUse\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getGeneralUse(), "DU should specify general use");
    assertValidJson(json);
    assertInvalidJson("{ \"generalUse\": \"string\" }");
  }

  @Test
  public void hmbResearch() throws Exception {
    String json = "{ \"hmbResearch\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getHmbResearch(), "DU should specify health/medical/biomedical research");
    assertValidJson(json);
    assertInvalidJson("{ \"hmbResearch\": \"string\" }");
  }

  @Test
  public void diseaseRestrictions() throws Exception {
    String json = "{ \"diseaseRestrictions\": [\"one\", \"two\"] }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals(2, dataUse.getDiseaseRestrictions().size(),
        "DU should specify disease restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"diseaseRestrictions\": \"string\" }");
  }

  @Test
  public void populationOriginsAncestry() throws Exception {
    String json = "{ \"populationOriginsAncestry\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPopulationOriginsAncestry(),
        "DU should specify population origins or ancestry use");
    assertValidJson(json);
    assertInvalidJson("{ \"populationOriginsAncestry\": \"string\" }");
  }

  @Test
  public void populationStructure() throws Exception {
    String json = "{ \"populationStructure\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPopulationStructure(), "DU should specify population structure research");
    assertValidJson(json);
    assertInvalidJson("{ \"populationStructure\": \"string\" }");
  }

  @Test
  public void commercialUse() throws Exception {
    String json = "{ \"commercialUse\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getCommercialUse(), "DU should specify commercial use");
    assertValidJson(json);
    assertInvalidJson("{ \"commercialUse\": \"string\" }");
  }

  @Test
  public void methodsResearch() throws Exception {
    String json = "{ \"methodsResearch\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getMethodsResearch(), "DU should specify methods research use");
    assertValidJson(json);
    assertInvalidJson("{ \"methodsResearch\": \"string\" }");
  }

  @Test
  public void aggregateResearch() throws Exception {
    String json = "{ \"aggregateResearch\": \"Yes\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("Yes", dataUse.getAggregateResearch(), "DU should specify aggregate research use");
    assertValidJson(json);
    assertInvalidJson("{ \"aggregateResearch\": \"string\" }");
  }

  @Test
  public void gender() throws Exception {
    String json = "{ \"gender\": \"Male\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("Male", dataUse.getGender(), "DU should specify Male gender");
    assertValidJson(json);
    assertInvalidJson("{ \"gender\": \"string\" }");
  }

  @Test
  public void controlSetOption() throws Exception {
    String json = "{ \"controlSetOption\": \"Yes\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("Yes", dataUse.getControlSetOption(), "DU should specify a control set usage");
    assertValidJson(json);
    assertInvalidJson("{ \"controlSetOption\": \"string\" }");
  }

  @Test
  public void populationRestrictions() throws Exception {
    String json = "{ \"populationRestrictions\": [\"one\", \"two\"] }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals(2, dataUse.getPopulationRestrictions().size(),
        "DU should specify a population restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"populationRestrictions\": \"string\" }");
  }

  @Test
  public void pediatric() throws Exception {
    String json = "{ \"pediatric\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPediatric(), "DU should specify a pediatric usage");
    assertValidJson(json);
    assertInvalidJson("{ \"pediatric\": \"string\" }");
  }

  @Test
  public void dateRestriction() throws Exception {
    String json = "{ \"dateRestriction\": \"date\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("date", dataUse.getDateRestriction(), "DU should specify a date restriction");
    assertValidJson(json);
    assertInvalidJson("{ \"dateRestriction\": true }");
  }

  @Test
  public void recontacting() throws Exception {
    String json = "{ \"recontactingDataSubjects\": true, \"recontactMay\": \"may\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getRecontactingDataSubjects(),
        "DU should specify whether recontacting subjects is allowed");
    assertValidJson(json);

    json = "{ \"recontactingDataSubjects\": true, \"recontactMust\": \"must\" }";
    dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getRecontactingDataSubjects(),
        "DU should specify whether recontacting subjects is allowed");
    assertValidJson(json);

    assertInvalidJson("{ \"recontactingDataSubjects\": \"string\" }");
    assertInvalidJson("{ \"recontactingDataSubjects\": true }");
    assertInvalidJson("{ \"recontactingDataSubjects\": true, \"dateRestriction\": \"date\" }");
    assertInvalidJson("{ \"recontactingDataSubjects\": true, \"recontactMay\": true }");
    assertInvalidJson("{ \"recontactingDataSubjects\": true, \"recontactMust\": true }");

  }

  @Test
  public void recontactMay() throws Exception {
    String json = "{ \"recontactMay\": \"may\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("may", dataUse.getRecontactMay(),
        "DU should specify conditions under which subjects *may* be recontacted");
    assertValidJson(json);
    assertInvalidJson("{ \"recontactMay\": true }");
  }

  @Test
  public void recontactMust() throws Exception {
    String json = "{ \"recontactMust\": \"must\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("must", dataUse.getRecontactMust(),
        "DU should specify conditions under which subjects *must* be recontacted");
    assertValidJson(json);
    assertInvalidJson("{ \"recontactMust\": true }");
  }

  @Test
  public void genomicPhenotypicData() throws Exception {
    String json = "{ \"genomicPhenotypicData\": \"Yes\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("Yes", dataUse.getGenomicPhenotypicData(),
        "DU should specify if genomic and phenotypic data is available for future research");
    assertValidJson(json);
    assertInvalidJson("{ \"genomicPhenotypicData\": true }");
  }

  @Test
  public void otherRestrictions() throws Exception {
    String json = "{ \"otherRestrictions\": true, \"other\": \"other\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getOtherRestrictions(), "DU should specify if there are other conditions");
    assertValidJson(json);
    assertValidJson(
        "{ \"otherRestrictions\": true, \"other\": \"other\", \"ethicsApprovalRequired\": true }");
    assertValidJson(
        "{ \"otherRestrictions\": true, \"other\": \"other\", \"geographicalRestrictions\": \"US\" }");
    assertValidJson(
        "{ \"otherRestrictions\": true, \"other\": \"other\", \"cloudStorage\": \"Yes\" }");
    assertValidJson(
        "{ \"otherRestrictions\": true, \"other\": \"other\", \"cloudStorage\": \"Yes\", \"ethicsApprovalRequired\": true }");
    assertInvalidJson("{ \"otherRestrictions\": true }");
    assertInvalidJson("{ \"otherRestrictions\": \"string\" }");
  }

  @Test
  public void other() throws Exception {
    String json = "{ \"other\": \"other\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("other", dataUse.getOther(), "DU should specify other restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"other\": true }");
  }

  @Test
  public void cloudStorage() throws Exception {
    String json = "{ \"cloudStorage\": \"Yes\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("Yes", dataUse.getCloudStorage(), "DU should specify cloud storage use");
    assertValidJson(json);
    assertInvalidJson("{ \"cloudStorage\": \"string\" }");
  }

  @Test
  public void geographicalRestrictions() throws Exception {
    String json = "{ \"geographicalRestrictions\": \"geo\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("geo", dataUse.getGeographicalRestrictions(),
        "DU should specify geographic restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"geographicalRestrictions\": true }");
  }

  @Test
  public void illegalBehavior() throws Exception {
    String json = "{ \"illegalBehavior\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getIllegalBehavior(), "DU should specify illegal behavior");
    assertValidJson(json);
    assertInvalidJson("{ \"illegalBehavior\": \"string\" }");
  }

  @Test
  public void addiction() throws Exception {
    String json = "{ \"addiction\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getAddiction());
    assertValidJson(json);
    assertInvalidJson("{ \"addiction\": \"string\" }");
  }

  @Test
  public void sexualDiseases() throws Exception {
    String json = "{ \"sexualDiseases\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getSexualDiseases());
    assertValidJson(json);
    assertInvalidJson("{ \"sexualDiseases\": \"string\" }");
  }

  @Test
  public void stigmatizeDiseases() throws Exception {
    String json = "{ \"stigmatizeDiseases\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getStigmatizeDiseases());
    assertValidJson(json);
    assertInvalidJson("{ \"stigmatizeDiseases\": \"string\" }");
  }

  @Test
  public void vulnerablePopulations() throws Exception {
    String json = "{ \"vulnerablePopulations\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getVulnerablePopulations());
    assertValidJson(json);
    assertInvalidJson("{ \"vulnerablePopulations\": \"string\" }");
  }

  @Test
  public void psychologicalTraits() throws Exception {
    String json = "{ \"psychologicalTraits\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPsychologicalTraits());
    assertValidJson(json);
    assertInvalidJson("{ \"psychologicalTraits\": \"string\" }");
  }

  @Test
  public void nonBiomedical() throws Exception {
    String json = "{ \"nonBiomedical\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getNonBiomedical());
    assertValidJson(json);
    assertInvalidJson("{ \"nonBiomedical\": \"string\" }");
  }

  @Test
  public void ethicsApprovalRequired() throws Exception {
    String json = "{ \"ethicsApprovalRequired\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getEthicsApprovalRequired());
    assertValidJson(json);
    assertInvalidJson("{ \"ethicsApprovalRequired\": \"string\" }");
  }

  @Test
  public void collaboratorRequired() throws Exception {
    String json = "{ \"collaboratorRequired\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getCollaboratorRequired());
    assertValidJson(json);
    assertInvalidJson("{ \"collaboratorRequired\": \"string\" }");
  }

  @Test
  public void testGeneticStudiesOnly() throws Exception {
    String json = "{ \"geneticStudiesOnly\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getGeneticStudiesOnly());
    assertValidJson(json);
    assertInvalidJson("{ \"geneticStudiesOnly\": \"string\" }");
  }

  @Test
  public void testPublicationResults() throws Exception {
    String json = "{ \"publicationResults\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPublicationResults());
    assertValidJson(json);
    assertInvalidJson("{ \"publicationResults\": \"string\" }");
  }

  @Test
  public void testGenomicResults() throws Exception {
    String json = "{ \"genomicResults\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getGenomicResults());
    assertValidJson(json);
    assertInvalidJson("{ \"genomicResults\": \"string\" }");
  }

  @Test
  public void testGenomicSummaryResults() throws Exception {
    String json = "{ \"genomicSummaryResults\": \"summary results\" }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertFalse(dataUse.getGenomicSummaryResults().isEmpty());
    assertValidJson(json);
    assertInvalidJson("{ \"genomicSummaryResults\": true }");
  }

  @Test
  public void testCollaborationInvestigators() throws Exception {
    String json = "{ \"collaborationInvestigators\": true }";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getCollaborationInvestigators());
    assertValidJson(json);
    assertInvalidJson("{ \"collaborationInvestigators\": \"string\" }");
  }

}
