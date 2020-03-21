package com.gmail.aazavoykin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.aazavoykin.rest.response.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BloggyAuthenticationEntryPoint extends ResponseHandler implements AuthenticationEntryPoint {

    public BloggyAuthenticationEntryPoint(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        putJsonToResponse(Response.accessDenied(), response);
    }
}
