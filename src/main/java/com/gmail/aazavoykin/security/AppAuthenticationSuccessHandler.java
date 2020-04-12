package com.gmail.aazavoykin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.aazavoykin.rest.response.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppAuthenticationSuccessHandler extends ResponseHandler implements AuthenticationSuccessHandler {

    public AppAuthenticationSuccessHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        putJsonToResponse(Response.success(), response);
    }
}
