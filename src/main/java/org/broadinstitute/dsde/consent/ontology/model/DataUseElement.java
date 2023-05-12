package org.broadinstitute.dsde.consent.ontology.model;

public class DataUseElement {

  String code;
  String description;

  public DataUseElement(String code, String description) {
    setCode(code);
    setDescription(description);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description.replaceAll("\\[.+]", "").trim();
  }
}
