package com.gmail.aazavoykin.rest.response;

import com.gmail.aazavoykin.exception.InternalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Response<T> {

    private T body;
    private String message;
    private String code;

    public static Response<Void> success() {
        return success(null);
    }

    public static <T> Response<T> success(T body) {
        return new Response<>(body, "OK", "200");
    }

    public static Response<Void> unknownError() {
        return new Response<>(null, "Error! Please, contact admin", "500");
    }

    public static Response<Void> error(InternalException e) {
        return new Response<>(null, e.getErrorType().getMessage(), e.getErrorType().getCode());
    }

    public static Response<Void> authFail() {
        return new Response<>(null, "Not valid credentials", "400");
    }

    public static Response<Void> accessDenied() {
        return new Response<>(null, "Access denied", "401");
    }
}
