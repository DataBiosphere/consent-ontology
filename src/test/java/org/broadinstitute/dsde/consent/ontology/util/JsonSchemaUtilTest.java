package org.broadinstitute.dsde.consent.ontology.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonSchemaUtilTest {

  private static final JsonSchemaUtil schemaUtil = new JsonSchemaUtil();

  @Test
  void testIsValidDataUseV3Object_case0() {
    String instance = "{}";
    List<String> errors = schemaUtil.validateDataUseV3Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testIsValidDataUseV3Object_case1() {
    String dataUseV3Instance = """
        {
          "generalUse": false,
          "diseaseRestrictions": ["test"],
          "hmbResearch": false,
          "populationOriginsAncestry": true,
          "methodsResearch": true,
          "commercialUse": true,
          "nonProfitUse": true,
          "other": "other",
          "secondaryOther": "secondaryOther",
          "ethicsApprovalRequired": true,
          "collaboratorRequired": true,
          "geographicalRestrictions": "geographicalRestrictions",
          "geneticStudiesOnly": true,
          "publicationResults": true,
          "publicationMoratorium": "2023-03-09"
        }
        """;
    List<String> errors = schemaUtil.validateDataUseV3Schema(dataUseV3Instance);
    assertTrue(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_valid() {
    String dataUseV4Instance = """
        {
          "generalUse": false,
          "diseaseRestrictions": ["test"],
          "hmbResearch": false,
          "populationOriginsAncestry": true,
          "methodsResearch": true,
          "nonProfitUse": true,
          "other": "other",
          "secondaryOther": "secondaryOther",
          "ethicsApprovalRequired": true,
          "collaboratorRequired": true,
          "geographicalRestrictions": "geographicalRestrictions",
          "geneticStudiesOnly": true,
          "publicationResults": true,
          "publicationMoratorium": "2023-03-09"
        }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(dataUseV4Instance);
    assertTrue(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase1() {
    String instance = "{}";
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      """
        {
          "generalUse": true,
          "diseaseRestrictions": ["test"],
          "hmbResearch": true
        }
      """,
      """
        {
          "generalUse": true,
          "populationOriginsAncestry": "test"
        }
      """,
      """
        {
          "generalUse": true,
          "methodsResearch": "test"
        }
      """,
      """
        {
          "generalUse": true,
          "nonProfitUse": "test"
        }
      """,
      """
        {
          "generalUse": true,
          "other": true
        }
      """,
      """
        {
          "generalUse": true,
          "secondaryOther": true
        }
      """,
      """
        {
          "generalUse": true,
          "ethicsApprovalRequired": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "collaboratorRequired": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "geographicalRestrictions": true
        }
      """,
      """
        {
          "generalUse": true,
          "geneticStudiesOnly": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "publicationResults": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "publicationMoratorium": true
        }
      """,
      """
        {
          "generalUse": true,
          "controls": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "gender": true
        }
      """,
      """
        {
          "generalUse": true,
          "pediatric": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "population": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "illegalBehavior": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "sexualDiseases": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "stigmatizeDiseases": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "vulnerablePopulations": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "psychologicalTraits": "true"
        }
      """,
      """
        {
          "generalUse": true,
          "notHealth": "true"
        }
      """
  })
  void testValidateDataUseV4SchemaInvalidCases(String instance) {
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

}
