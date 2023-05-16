package org.broadinstitute.dsde.consent.ontology.model;

import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;

@SuppressWarnings("unused")
public class DataUseMatchPairV3 {

  private DataUseV3 purpose;

  private DataUseV3 consent;

  public DataUseMatchPairV3() {
  }

  public DataUseMatchPairV3(DataUseV3 purpose, DataUseV3 consent) {
    this.purpose = purpose;
    this.consent = consent;
  }

  public DataUseV3 getPurpose() {
    return purpose;
  }

  public void setPurpose(DataUseV3 purpose) {
    this.purpose = purpose;
  }

  public DataUseV3 getConsent() {
    return consent;
  }

  public void setConsent(DataUseV3 consent) {
    this.consent = consent;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if (!(other instanceof DataUseMatchPairV3 rhs)) {
      return false;
    }
    return new EqualsBuilder()
        .append(purpose, rhs.purpose)
        .append(consent, rhs.consent)
        .isEquals();
  }
}
