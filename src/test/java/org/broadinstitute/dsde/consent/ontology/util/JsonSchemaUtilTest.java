package org.broadinstitute.dsde.consent.ontology.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsonSchemaUtilTest {

  private final static JsonSchemaUtil schemaUtil = new JsonSchemaUtil();

  private final String dataUseV3Instance = """
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

  private final String dataUseV4Instance = """
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

  @Test
  void testIsValidDataUseV3Object_case0() {
    String instance = "{}";
    List<String> errors = schemaUtil.validateDataUseV3Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testIsValidDataUseV3Object_case1() {
    List<String> errors = schemaUtil.validateDataUseV3Schema(dataUseV3Instance);
    assertTrue(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_valid() {
    List<String> errors = schemaUtil.validateDataUseV4Schema(dataUseV4Instance);
    assertTrue(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase1() {
    String instance = "{}";
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase2() {
    String instance = """
            {
              "generalUse": true,
              "diseaseRestrictions": ["test"],
              "hmbResearch": true
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase3() {
    String instance = """
            {
              "generalUse": true,
              "populationOriginsAncestry": "test"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase4() {
    String instance = """
            {
              "generalUse": true,
              "methodsResearch": "test"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase5() {
    String instance = """
            {
              "generalUse": true,
              "nonProfitUse": "test"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase6() {
    String instance = """
            {
              "generalUse": true,
              "other": true
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase7() {
    String instance = """
            {
              "generalUse": true,
              "secondaryOther": true
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase8() {
    String instance = """
            {
              "generalUse": true,
              "ethicsApprovalRequired": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase9() {
    String instance = """
            {
              "generalUse": true,
              "collaboratorRequired": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase10() {
    String instance = """
            {
              "generalUse": true,
              "geographicalRestrictions": true
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase11() {
    String instance = """
            {
              "generalUse": true,
              "geneticStudiesOnly": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase12() {
    String instance = """
            {
              "generalUse": true,
              "publicationResults": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase13() {
    String instance = """
            {
              "generalUse": true,
              "publicationMoratorium": true
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase14() {
    String instance = """
            {
              "generalUse": true,
              "controls": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase15() {
    String instance = """
            {
              "generalUse": true,
              "gender": true
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase16() {
    String instance = """
            {
              "generalUse": true,
              "pediatric": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase17() {
    String instance = """
            {
              "generalUse": true,
              "population": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase18() {
    String instance = """
            {
              "generalUse": true,
              "illegalBehavior": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase19() {
    String instance = """
            {
              "generalUse": true,
              "sexualDiseases": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase20() {
    String instance = """
            {
              "generalUse": true,
              "stigmatizeDiseases": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase21() {
    String instance = """
            {
              "generalUse": true,
              "vulnerablePopulations": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);

    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase22() {
    String instance = """
            {
              "generalUse": true,
              "psychologicalTraits": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  void testValidateDataUseV4Schema_invalidCase23() {
    String instance = """
            {
              "generalUse": true,
              "notHealth": "true"
            }
        """;
    List<String> errors = schemaUtil.validateDataUseV4Schema(instance);
    assertFalse(errors.isEmpty());
  }

}
