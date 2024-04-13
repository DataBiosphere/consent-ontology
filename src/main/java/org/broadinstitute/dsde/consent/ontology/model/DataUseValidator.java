package org.broadinstitute.dsde.consent.ontology.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Validator is used to make fine-grained checks on different combinations of Data Use questions.
 */
public class DataUseValidator {

  private DataUse dataUse = null;
  private List<String> validationErrors = null;

  public DataUseValidator(DataUse dataUse) {
    this.dataUse = dataUse;
    this.validationErrors = new ArrayList<>();
    validate();
  }

  public Boolean getIsValid() {
    return validationErrors.isEmpty();
  }

  public List<String> getValidationErrors() {
    return validationErrors;
  }

  /**
   * Internal validation method called when validator is instantiated.
   */
  private void validate() {
    if (dataUse == null) {
      validationErrors.add("Data Use cannot be null");
    }
  }
}
