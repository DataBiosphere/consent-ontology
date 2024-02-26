package org.broadinstitute.dsde.consent.ontology.model;

import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;

@SuppressWarnings("unused")
public class DataUseMatchPairV4 {

  private DataUseV4 purpose;

  private DataUseV4 consent;

  public DataUseMatchPairV4() {
  }

  public DataUseMatchPairV4(DataUseV4 purpose, DataUseV4 consent) {
    this.purpose = purpose;
    this.consent = consent;
  }

  public DataUseV4 getPurpose() {
    return purpose;
  }

  public void setPurpose(DataUseV4 purpose) {
    this.purpose = purpose;
  }

  public DataUseV4 getConsent() {
    return consent;
  }

  public void setConsent(DataUseV4 consent) {
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
    if (!(other instanceof DataUseMatchPairV4 rhs)) {
      return false;
    }
    return new EqualsBuilder()
        .append(purpose, rhs.purpose)
        .append(consent, rhs.consent)
        .isEquals();
  }
}
