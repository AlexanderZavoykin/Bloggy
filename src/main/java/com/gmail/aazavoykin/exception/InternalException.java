package com.gmail.aazavoykin.exception;

import lombok.Getter;

@Getter
public class InternalException extends RuntimeException {

    private final InternalErrorType errorType;

    public InternalException(InternalErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
