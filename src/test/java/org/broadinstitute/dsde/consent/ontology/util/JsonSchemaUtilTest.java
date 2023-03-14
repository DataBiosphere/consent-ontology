package org.broadinstitute.dsde.consent.ontology.util;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonSchemaUtilTest {

  private static JsonSchemaUtil schemaUtil;

  private final String dataUseV3Instance = """
      {
      "generalUse": true,
      "diseaseRestrictions": ["test"],
      "hmbResearch": true,
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

  @BeforeClass
  public static void setUp() {
    schemaUtil = new JsonSchemaUtil();
  }

  @Test
  public void testIsValidDataUseV3Object_case0() {
    String instance = "{}";
    List<String> errors = schemaUtil.validateDataUseV3Schema(instance);
    assertFalse(errors.isEmpty());
  }

  @Test
  public void testIsValidDataUseV3Object_case1() {
    List<String> errors = schemaUtil.validateDataUseV3Schema(dataUseV3Instance);
    assertTrue(errors.isEmpty());
  }
}