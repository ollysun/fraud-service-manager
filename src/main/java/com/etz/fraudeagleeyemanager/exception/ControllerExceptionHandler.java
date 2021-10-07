package com.etz.fraudeagleeyemanager.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),ex.getMessage(), HttpStatus.NOT_FOUND, details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleException(DataIntegrityViolationException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), "Data Integrity Violation",HttpStatus.CONFLICT,details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

//    @ExceptionHandler(UnAuthorizedException.class)
//    public ResponseEntity<Object> handleException(UnAuthorizedException ex, WebRequest request) {
//        List<String> details = new ArrayList<>();
//        details.add(ex.getLocalizedMessage());
//        final ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, new Date(),
//                ex.getMessage(), details);
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleException(DuplicateKeyException ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                "Similar record already exist",HttpStatus.CONFLICT,details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SqlExceptionError.class)
    public ResponseEntity<Object> handleException(SqlExceptionError ex){
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                "Similar record already exist",HttpStatus.CONFLICT,details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ex.printStackTrace();
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), "You do not have the needed permission to access the endpoint",
                HttpStatus.FORBIDDEN, details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
    	List<String> details = new ArrayList<>();
    	details.add(ex.getLocalizedMessage());
    	ex.printStackTrace();
    	final ExceptionResponse exceptionResponse = new ExceptionResponse(
    			new Date(), "Something went wrong while trying to process your request",
    			HttpStatus.INTERNAL_SERVER_ERROR,
    			details);
    	return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(FraudEngineException.class)
    public ResponseEntity<Object> handlePersonException(FraudEngineException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                 new Date(), ex.getMessage(),HttpStatus.BAD_REQUEST,
                details);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(), null,HttpStatus.BAD_REQUEST,
                errors);
        return handleExceptionInternal(ex, exceptionResponse, headers, exceptionResponse.getStatus(), request);

    }


}
