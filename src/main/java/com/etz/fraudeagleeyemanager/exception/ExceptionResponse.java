package com.etz.fraudeagleeyemanager.exception;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ExceptionResponse {

    private Date dateofError;
    private String message;
    private HttpStatus status;
    private List<String> errors;

    public ExceptionResponse() {
        super();
    }

    public ExceptionResponse(Date dateofError, String message, HttpStatus status, List<String> errors) {
        super();
        this.dateofError = dateofError;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public ExceptionResponse(Date dateofError, String message, HttpStatus status) {
        this.dateofError = dateofError;
        this.message = message;
        this.status = status;
    }
}
