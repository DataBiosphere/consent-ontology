package org.broadinstitute.dsde.consent.ontology.model;

import java.util.ArrayList;
import java.util.Collection;

public class ValidationResponse {

  private boolean valid;
  private String useRestriction;
  private final Collection<String> errors;

  public ValidationResponse(boolean valid, String useRestriction) {
    this.valid = valid;
    this.useRestriction = useRestriction;
    this.errors = new ArrayList<>();
  }

  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public String getUseRestriction() {
    return useRestriction;
  }

  public void setUseRestriction(String useRestriction) {
    this.useRestriction = useRestriction;
  }

  public Collection<String> getErrors() {
    return errors;
  }

  public void addError(String error) {
    this.errors.add(error);
  }


  public void addErrors(Collection<String> errors) {
    this.errors.addAll(errors);
  }
}
