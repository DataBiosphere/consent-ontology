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
    String DU_CONTENT = FileUtils.readAllTextFromResource("data-use-v4.json");
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
    String json = """
        { "generalUse": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getGeneralUse(), "DU should specify general use");
    assertValidJson(json);
    assertInvalidJson("{ \"generalUse\": \"string\" }");
  }

  @Test
  public void hmbResearch() throws Exception {
    String json = """
        { "hmbResearch": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getHmbResearch(), "DU should specify health/medical/biomedical research");
    assertValidJson(json);
    assertInvalidJson("{ \"hmbResearch\": \"string\" }");
  }

  @Test
  public void diseaseRestrictions() throws Exception {
    String json = """
        { "diseaseRestrictions": ["one", "two"] }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals(2, dataUse.getDiseaseRestrictions().size(),
        "DU should specify disease restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"diseaseRestrictions\": \"string\" }");
  }

  @Test
  public void populationOriginsAncestry() throws Exception {
    String json = """
        {  "hmbResearch": true, "populationOriginsAncestry": true}""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPopulationOriginsAncestry(),
        "DU should specify population origins or ancestry use");
    assertValidJson(json);
    assertInvalidJson("{ \"populationOriginsAncestry\": \"string\" }");
  }

  @Test
  public void populationStructure() throws Exception {
    String json = """
        {  "hmbResearch": true,  "population": true}""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPopulation(), "DU should specify population");
    assertValidJson(json);
    assertInvalidJson("{ \"populationStructure\": \"string\" }");
  }

  @Test
  public void nonProfitUse() throws Exception {
    String json = """
        {    "hmbResearch": true, "nonProfitUse": true  }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getNonProfitUse(), "DU should specify nonProfitUse use");
    assertValidJson(json);
    assertInvalidJson("{ \"commercialUse\": \"string\" }");
  }

  @Test
  public void methodsResearch() throws Exception {
    String json = """
        { "hmbResearch": true, "methodsResearch": true  }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getMethodsResearch(), "DU should specify methods research use");
    assertValidJson(json);
    assertInvalidJson("{ \"methodsResearch\": \"string\" }");
  }

  @Test
  public void gender() throws Exception {
    String json = """
        { "hmbResearch": true, "gender": "Male"}""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("Male", dataUse.getGender(), "DU should specify Male gender");
    assertValidJson(json);
    assertInvalidJson("{ \"gender\": \"string\" }");
  }

  @Test
  public void controls() throws Exception {
    String json = """
        { "hmbResearch": true, "controls": true}""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getControls(), "DU should specify controls");
    assertValidJson(json);
    assertInvalidJson("{ \"controls\": \"string\" }");
  }

  @Test
  public void population() throws Exception {
    String json = """
        { "hmbResearch": true, "population": true}""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPopulation(), "DU should specify a population restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"population\": \"string\" }");
  }

  @Test
  public void pediatric() throws Exception {
    String json = """
        { "hmbResearch": true, "pediatric": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPediatric(), "DU should specify a pediatric usage");
    assertValidJson(json);
    assertInvalidJson("{ \"pediatric\": \"string\" }");
  }

  @Test
  public void other() throws Exception {
    String json = """
        { "hmbResearch": true, "other": "other" }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("other", dataUse.getOther(), "DU should specify other restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"other\": true }");
  }

  @Test
  public void geographicalRestrictions() throws Exception {
    String json = """
        { "hmbResearch": true, "geographicalRestrictions": "geo" }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertEquals("geo", dataUse.getGeographicalRestrictions(),
        "DU should specify geographic restrictions");
    assertValidJson(json);
    assertInvalidJson("{ \"geographicalRestrictions\": true }");
  }

  @Test
  public void illegalBehavior() throws Exception {
    String json = """
        { "hmbResearch": true, "illegalBehavior": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getIllegalBehavior(), "DU should specify illegal behavior");
    assertValidJson(json);
    assertInvalidJson("{ \"illegalBehavior\": \"string\" }");
  }

  @Test
  public void sexualDiseases() throws Exception {
    String json = """
        { "hmbResearch": true, "sexualDiseases": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getSexualDiseases());
    assertValidJson(json);
    assertInvalidJson("{ \"sexualDiseases\": \"string\" }");
  }

  @Test
  public void stigmatizeDiseases() throws Exception {
    String json = """
        { "hmbResearch": true, "stigmatizeDiseases": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getStigmatizeDiseases());
    assertValidJson(json);
    assertInvalidJson("{ \"stigmatizeDiseases\": \"string\" }");
  }

  @Test
  public void vulnerablePopulations() throws Exception {
    String json = """
        { "hmbResearch": true, "vulnerablePopulations": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getVulnerablePopulations());
    assertValidJson(json);
    assertInvalidJson("{ \"vulnerablePopulations\": \"string\" }");
  }

  @Test
  public void psychologicalTraits() throws Exception {
    String json = """
        { "hmbResearch": true, "psychologicalTraits": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPsychologicalTraits());
    assertValidJson(json);
    assertInvalidJson("{ \"psychologicalTraits\": \"string\" }");
  }

  @Test
  public void notHealth() throws Exception {
    String json = """
        { "hmbResearch": true, "notHealth": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getNotHealth());
    assertValidJson(json);
    assertInvalidJson("{ \"notHealth\": \"string\" }");
  }

  @Test
  public void ethicsApprovalRequired() throws Exception {
    String json = """
        { "hmbResearch": true, "ethicsApprovalRequired": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getEthicsApprovalRequired());
    assertValidJson(json);
    assertInvalidJson("{ \"ethicsApprovalRequired\": \"string\" }");
  }

  @Test
  public void collaboratorRequired() throws Exception {
    String json = """
        { "hmbResearch": true, "collaboratorRequired": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getCollaboratorRequired());
    assertValidJson(json);
    assertInvalidJson("{ \"collaboratorRequired\": \"string\" }");
  }

  @Test
  public void testGeneticStudiesOnly() throws Exception {
    String json = """
        { "hmbResearch": true, "geneticStudiesOnly": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getGeneticStudiesOnly());
    assertValidJson(json);
    assertInvalidJson("{ \"geneticStudiesOnly\": \"string\" }");
  }

  @Test
  public void testPublicationResults() throws Exception {
    String json = """
        { "hmbResearch": true, "publicationResults": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getPublicationResults());
    assertValidJson(json);
    assertInvalidJson("{ \"publicationResults\": \"string\" }");
  }

  @Test
  public void testCollaboratorRequired() throws Exception {
    String json = """
        { "hmbResearch": true, "collaboratorRequired": true }""";
    DataUse dataUse = MAPPER.readValue(json, DataUse.class);
    assertTrue(dataUse.getCollaboratorRequired());
    assertValidJson(json);
    assertInvalidJson("{ \"collaboratorRequired\": \"string\" }");
  }

}
