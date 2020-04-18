package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.db.repository.LoginAttemptRepository;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    private static final String TOKEN_NAME = "bloggytoken";
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @MockBean
    private LoginAttemptRepository loginAttemptRepository;

    private static HttpEntity<?> prepareHttpEntity(Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", token);
        headers.add("Content-Type", "application/json;charset=utf-8");
        return new HttpEntity<>(body, headers);
    }

    @Test
    void getUnactivatedUserDto_byUnauthorized() {
        final ResponseEntity<Response<Void>> response = restTemplate.exchange("/users/unactivated",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> body = response.getBody();
        Assertions.assertThrows(InternalException.class, () -> userService.getByNickname("unactivated"));
        assertNotNull(body);
        assertEquals("User not found", body.getMessage());
        assertEquals("E001", body.getCode());
    }

    @Test
    void getUnactivatedUserDto_bySingleUser() {
        final ResponseEntity<Response<Void>> response = restTemplate.exchange("/users/unactivated",
            HttpMethod.GET,
            prepareHttpEntity(null, getAccessToken("activated@test.com", "testtest")),
            new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> body = response.getBody();
        Assertions.assertThrows(InternalException.class, () -> userService.getByNickname("unactivated"));
        assertNotNull(body);
        assertEquals("User not found", body.getMessage());
        assertEquals("E001", body.getCode());
    }

    @Test
    void getUnactivatedUserDto_byAdmin() {
        final ResponseEntity<Response<UserDto>> response = restTemplate.exchange("/users/unactivated",
            HttpMethod.GET,
            prepareHttpEntity(null, getAccessToken("admin@test.com", "testtest")),
            new ParameterizedTypeReference<Response<UserDto>>() {
            });
        final Response<UserDto> body = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        assertNotNull(body);
        final UserDto userDto = body.getBody();
        assertNotNull(userDto);
        assertEquals(Long.valueOf(1000001L), userDto.getId());
        assertEquals("Unactivated", userDto.getNickname());
        assertEquals("Unactivated test user", userDto.getInfo());
        assertEquals("unactivated@test.com", userDto.getEmail());
    }

    @Test
    void getExistingActivatedUserDto_byUnauthorized() {
        final ResponseEntity<Response<UserDto>> response = restTemplate.exchange("/users/activated",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<UserDto>>() {
            });
        final Response<UserDto> body = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        assertNotNull(body);
        final UserDto userDto = body.getBody();
        assertNotNull(userDto);
        assertEquals(Long.valueOf(1000002L), userDto.getId());
        assertEquals("Activated", userDto.getNickname());
        assertEquals("Activated test user", userDto.getInfo());
        assertEquals("NOT_AVAILABLE", userDto.getEmail());
    }

    private String getAccessToken(String email, String password) {
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
            .orElse(null);
    }
}