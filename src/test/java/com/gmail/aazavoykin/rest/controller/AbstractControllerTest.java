package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.db.repository.LoginAttemptRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public abstract class AbstractControllerTest {

    protected static final String TOKEN_NAME = "bloggytoken";
    protected static final String SINGLE_USER_EMAIL = "second@test.com";
    protected static final String SINGLE_USER_PASSWORD = "testtest";
    protected static final String ADMIN_EMAIL = "third@test.com";
    protected static final String ADMIN_PASSWORD = "testtest";
    @Autowired
    protected TestRestTemplate restTemplate;
    @MockBean
    private LoginAttemptRepository loginAttemptRepository;

    protected static HttpEntity<?> prepareHttpEntity(Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", token);
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new HttpEntity<>(body, headers);
    }

    protected String getSingleUserAccessToken() {
        return getAccessToken(SINGLE_USER_EMAIL, SINGLE_USER_PASSWORD);
    }

    protected String getAdminAccessToken() {
        return getAccessToken(ADMIN_EMAIL, ADMIN_PASSWORD);
    }

    protected String getAccessToken(String email, String password) {
        when(loginAttemptRepository.countFailedLoginAttemptsLast30Mins(anyLong())).thenReturn(0L);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("login", email);
        body.add("password", password);
        final HttpEntity<?> entity = new HttpEntity<>(body, headers);
        ResponseEntity<?> response = restTemplate.exchange("/login", HttpMethod.POST, entity, Object.class, new Object());
        return Optional.ofNullable(response.getHeaders().get("Set-Cookie"))
            .flatMap(headerValues -> headerValues.stream()
                .filter(value -> value.startsWith(TOKEN_NAME))
                .findFirst())
            .orElseThrow(() -> new AssertionError("Could not login"));
    }
}
