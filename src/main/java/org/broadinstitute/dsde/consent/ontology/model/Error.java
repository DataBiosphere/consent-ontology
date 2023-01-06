package org.broadinstitute.dsde.consent.ontology.model;

public class Error {

  private String message;

  private Integer code;

  public Error() {
  }

  public Error(String message, Integer code) {
    this.message = message;
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String error) {
    this.message = error;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

}
