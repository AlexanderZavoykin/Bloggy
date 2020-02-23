package com.gmail.aazavoykin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InternalErrorType {

    ENTITY_NOT_FOUND("E001", "Entity not found"),
    ENTITY_ALREADY_EXISTS("E002","Entity already exists"),
    OPERATION_NOT_AVAILABLE("E003", "Operation is not available for this user");

    private final String code;

    private final String message;

}
