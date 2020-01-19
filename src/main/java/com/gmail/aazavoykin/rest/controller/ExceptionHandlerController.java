package com.gmail.aazavoykin.rest.controller;

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
public class ExceptionHandlerController extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(BindException.class)
    @Override
    protected ModelAndView handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        final ModelAndView modelAndView = new ModelAndView("exception");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("errors", ex.getFieldErrors());
        return modelAndView;
    }

}
