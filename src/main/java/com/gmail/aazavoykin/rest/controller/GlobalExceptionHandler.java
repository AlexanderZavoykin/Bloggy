package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler
    public Response<Void> handle(InternalException e) {
        return Response.error(e);
    }

}
