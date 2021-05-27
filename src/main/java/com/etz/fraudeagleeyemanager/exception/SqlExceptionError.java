package com.etz.fraudeagleeyemanager.exception;


import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.spi.SQLExceptionConverter;

public class SqlExceptionError extends Throwable {
    public SqlExceptionError(String message) {
        super(message);
    }
}
