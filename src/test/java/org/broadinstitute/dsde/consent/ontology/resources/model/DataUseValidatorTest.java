package org.broadinstitute.dsde.consent.ontology.resources.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.broadinstitute.dsde.consent.ontology.model.DataUse;
import org.broadinstitute.dsde.consent.ontology.model.DataUseValidator;
import org.junit.jupiter.api.Test;

public class DataUseValidatorTest {

  @Test
  public void testNull() {
    DataUseValidator validator = new DataUseValidator(null);
    assertFalse(validator.getIsValid());
  }

  @Test
  public void testGeneralUse() {

    DataUse dataUse = new DataUse();
    dataUse.setGeneralUse(true);
    DataUseValidator validator = new DataUseValidator(dataUse);
    assertTrue(validator.getIsValid());
  }

  @Test
  public void testOther() {
    DataUse dataUse = new DataUse();
    dataUse.setOther("weekends");
    DataUseValidator validator = new DataUseValidator(dataUse);
    assertTrue(validator.getIsValid());

    dataUse = new DataUse();
    dataUse.setSecondaryOther("secondary");
    validator = new DataUseValidator(dataUse);
    assertTrue(validator.getIsValid());

    dataUse = new DataUse();
    dataUse.setOther("weekends");
    dataUse.setEthicsApprovalRequired(true);
    validator = new DataUseValidator(dataUse);
    assertTrue(validator.getIsValid());

    dataUse = new DataUse();
    dataUse.setOther("weekends");
    dataUse.setGeographicalRestrictions("US");
    validator = new DataUseValidator(dataUse);
    assertTrue(validator.getIsValid());

  }

}
