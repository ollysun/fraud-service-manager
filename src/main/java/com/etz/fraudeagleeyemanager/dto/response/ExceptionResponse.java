package com.etz.fraudeagleeyemanager.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {
    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("errors")
    private Object errors = null;
}