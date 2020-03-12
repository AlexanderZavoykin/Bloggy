package com.gmail.aazavoykin.rest.response;

import com.gmail.aazavoykin.exception.InternalException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class Response<T> {

    private T body;
    private String message;
    private int code;

    private Response(T body, String message, HttpStatus httpStatus) {
        this.body = body;
        this.message = message;
        code = httpStatus.value();
    }

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T body) {
        return new Response<>(body, "OK", HttpStatus.OK);
    }

    public static <T> Response<T> error(Exception e) {
        return new Response<>(null, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
