package com.gmail.aazavoykin.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundRestException extends RuntimeException {

    private final String message;

    private final HttpStatus httpStatus;

    public EntityNotFoundRestException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
