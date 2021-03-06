package com.gmail.aazavoykin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.aazavoykin.rest.response.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppAuthenticationFailureHandler extends ResponseHandler implements AuthenticationFailureHandler {

    public AppAuthenticationFailureHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        putJsonToResponse(Response.authFail(), response);
    }
}
