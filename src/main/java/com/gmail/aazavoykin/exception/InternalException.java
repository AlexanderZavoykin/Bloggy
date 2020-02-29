package com.gmail.aazavoykin.exception;

import lombok.Getter;

@Getter
public class InternalException extends RuntimeException {

    private InternalErrorType errorType;

    public InternalException(InternalErrorType errorType) {
        super(errorType.name() + ":" + errorType.getMessage());
        this.errorType = errorType;
    }
}
