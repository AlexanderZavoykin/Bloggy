package com.gmail.aazavoykin.exception;

public class InternalException extends RuntimeException {

    public InternalException(InternalErrorType errorType) {
        super(errorType.name() + ":" + errorType.getMessage());
    }
}
