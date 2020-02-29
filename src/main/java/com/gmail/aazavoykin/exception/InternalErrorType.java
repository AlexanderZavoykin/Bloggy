package com.gmail.aazavoykin.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InternalErrorType {

    USER_NOT_FOUND("E001", "User not found"),
    STORY_NOT_FOUND("E002", "Story not found"),
    TAG_NOT_FOUND("E003", "Tag not found"),
    COMMENT_NOT_FOUND("E004", "Comment not found"),
    USER_ALREADY_EXISTS("E002", "User already exists"),
    TAG_ALREADY_EXISTS("E003","Tag already exists"),

    OPERATION_NOT_AVAILABLE("E009", "Operation is not available for this user"),

    NOT_VALID_ARGUMENT("S001", "Argument is not valid");

    private final String code;

    private final String message;

}
