package com.etz.fraudeagleeyemanager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
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
