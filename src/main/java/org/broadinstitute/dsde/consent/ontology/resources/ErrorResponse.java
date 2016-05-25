package org.broadinstitute.dsde.consent.ontology.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty
    private String message;

    @JsonProperty
    private Integer code;

    public ErrorResponse(String message, Integer code) {
        setMessage(message);
        setCode(code);
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
