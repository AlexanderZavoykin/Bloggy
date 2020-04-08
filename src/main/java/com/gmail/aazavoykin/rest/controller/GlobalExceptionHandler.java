package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Void> handle(Exception e, WebRequest request) {
        log.error("Unknown error {} on request {}", e, request);
        return Response.unknownError();
    }

    @ExceptionHandler(InternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<Void> handle(InternalException e) {
        log.error("Internal error ", e);
        return Response.error(e.getMessage());
    }
}
