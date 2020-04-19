package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.db.repository.UserRepository;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.rest.request.ChangePasswordRequest;
import com.gmail.aazavoykin.rest.request.ForgotPasswordRequest;
import com.gmail.aazavoykin.rest.request.UpdateUserInfoRequest;
import com.gmail.aazavoykin.rest.request.UserSignupRequest;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.MailService;
import com.gmail.aazavoykin.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private MailService mailService;

    @Test
    public void shouldGetOnlyEnabledUsers_byUnauthenticated() {
        final ResponseEntity<Response<List<UserDto>>> response = restTemplate.exchange("/users/all",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<List<UserDto>>>() {
            });
        final Response<List<UserDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<UserDto> userDtos = body.getBody();
        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        final UserDto userDto = userDtos.get(0);
        assertNotNull(userDto);
        assertEquals(1000002L, (long) userDto.getId());
    }

    @Test
    public void shouldAllUsers_byAdmin() {
        final ResponseEntity<Response<List<UserDto>>> response = restTemplate.exchange("/users/all",
            HttpMethod.GET,
            prepareHttpEntity(null, getAdminAccessToken()),
            new ParameterizedTypeReference<Response<List<UserDto>>>() {
            });
        final Response<List<UserDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<UserDto> userDtos = body.getBody();
        assertNotNull(userDtos);
        assertEquals(3, userDtos.size());
    }

    @Test
    public void shouldNotGetUnactivatedUserDto_byUnauthenticated() {
        final ResponseEntity<Response<Void>> response = restTemplate.exchange("/users/donaldduck",
            HttpMethod.GET, null, new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> body = response.getBody();
        Assertions.assertThrows(InternalException.class, () -> userService.getByNickname("unactivated"));
        assertNotNull(body);
        assertEquals("User not found", body.getMessage());
        assertEquals("E001", body.getCode());
    }

    @Test
    public void shouldNotGetUnactivatedUserDto_bySingleUser() {
        final ResponseEntity<Response<Void>> response = restTemplate.exchange("/users/donaldduck",
            HttpMethod.GET,
            prepareHttpEntity(null, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> body = response.getBody();
        Assertions.assertThrows(InternalException.class, () -> userService.getByNickname("unactivated"));
        assertNotNull(body);
        assertEquals("User not found", body.getMessage());
        assertEquals("E001", body.getCode());
    }

    @Test
    public void shouldGetUnactivatedUserDto_byAdmin() {
        final ResponseEntity<Response<UserDto>> response = restTemplate.exchange("/users/donaldduck",
            HttpMethod.GET,
            prepareHttpEntity(null, getAdminAccessToken()),
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
        assertEquals("DonaldDuck", userDto.getNickname());
        assertEquals("Unactivated test user", userDto.getInfo());
        assertEquals("first@test.com", userDto.getEmail());
    }

    @Test
    public void shouldGetActivatedUserDto_byUnauthenticated() {
        final ResponseEntity<Response<UserDto>> response = restTemplate.exchange("/users/charliesheen",
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
        assertEquals("CharlieSheen", userDto.getNickname());
        assertEquals("Activated test user", userDto.getInfo());
        assertEquals("NOT_AVAILABLE", userDto.getEmail());
    }

    @Test
    public void shouldNotGetAdminUserDto_bySingleUser() {
        final ResponseEntity<Response<Void>> response = restTemplate.exchange("/users/holyadmin",
            HttpMethod.GET,
            prepareHttpEntity(null, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> body = response.getBody();
        Assertions.assertThrows(InternalException.class, () -> userService.getByNickname("unactivated"));
        assertNotNull(body);
        assertEquals("User not found", body.getMessage());
        assertEquals("E001", body.getCode());
    }

    @Test
    public void shouldGetOnlyOneDto_whenGetAll_bySingleUser() {
        final ResponseEntity<Response<List<UserDto>>> response = restTemplate.exchange("/users/all",
            HttpMethod.GET,
            prepareHttpEntity(null, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<List<UserDto>>>() {
            });
        final Response<List<UserDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<UserDto> userDtos = body.getBody();
        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        final UserDto userDto = userDtos.get(0);
        assertEquals(Long.valueOf(1000002L), userDto.getId());
        assertEquals("CharlieSheen", userDto.getNickname());
        assertEquals("Activated test user", userDto.getInfo());
        assertEquals("NOT_AVAILABLE", userDto.getEmail());
    }

    @Test
    public void shouldGetAllDtos_whenGetAll_byAdmin() {
        final ResponseEntity<Response<List<UserDto>>> response = restTemplate.exchange("/users/all",
            HttpMethod.GET,
            prepareHttpEntity(null, getAdminAccessToken()),
            new ParameterizedTypeReference<Response<List<UserDto>>>() {
            });
        final Response<List<UserDto>> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        final List<UserDto> userDtos = body.getBody();
        assertNotNull(userDtos);
        assertEquals(3, userDtos.size());
        userDtos.forEach(Assert::assertNotNull);
    }

    @Test
    @DirtiesContext
    public void shouldSignupAndActivate() {
        final String email = "newone@mail.com";
        final UserSignupRequest request = new UserSignupRequest();
        request.setEmail(email);
        request.setNickname("hellsamurai");
        request.setPassword("Rhguslaagg@4");
        request.setMatchingPassword(request.getPassword());

        final ResponseEntity<Response<Void>> signupResponse = restTemplate.exchange("/users/auth/signup",
            HttpMethod.POST,
            prepareHttpEntity(request, null),
            new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> signupResponseBody = signupResponse.getBody();
        assertNotNull(signupResponseBody);
        assertEquals("OK", signupResponseBody.getMessage());
        assertEquals("200", signupResponseBody.getCode());
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mailService, times(1)).sendActivationUrl(anyString(), captor.capture());

        final ResponseEntity<Response<Void>> activationResponse =
            restTemplate.exchange("/users/auth/activate/" + captor.getValue(),
                HttpMethod.POST,
                prepareHttpEntity(request, null),
                new ParameterizedTypeReference<Response<Void>>() {
                });
        final Response<Void> activationResponseBody = activationResponse.getBody();
        assertNotNull(activationResponseBody);
        assertEquals("OK", activationResponseBody.getMessage());
        assertEquals("200", activationResponseBody.getCode());
        verify(mailService, times(1)).sendSuccessfulRegistrationConfirmation(email);
    }

    @Test
    @DirtiesContext
    public void shouldChangePasword() {
        final String email = "second@test.com";
        final String newPassword = "kfjjUY8833@gga";

        final ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail(email);
        final ResponseEntity<Response<Void>> forgotPasswordResponse = restTemplate.exchange("/users/password/forgot",
            HttpMethod.POST,
            prepareHttpEntity(forgotPasswordRequest, null),
            new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> forgotPasswordResponseBody = forgotPasswordResponse.getBody();
        assertNotNull(forgotPasswordResponseBody);
        assertEquals("OK", forgotPasswordResponseBody.getMessage());
        assertEquals("200", forgotPasswordResponseBody.getCode());
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mailService, times(1)).sendPasswordResetUrl(anyString(), captor.capture());


        final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setPassword(newPassword);
        changePasswordRequest.setMatchingPassword(newPassword);
        final ResponseEntity<Response<Void>> changePasswordResponse =
            restTemplate.exchange("/users/password/change/" + captor.getValue(),
                HttpMethod.POST,
                prepareHttpEntity(changePasswordRequest, null),
                new ParameterizedTypeReference<Response<Void>>() {
                });
        final Response<Void> changePasswordResponseBody = forgotPasswordResponse.getBody();
        assertNotNull(changePasswordResponseBody);
        assertEquals("OK", changePasswordResponseBody.getMessage());
        assertEquals("200", changePasswordResponseBody.getCode());
        verify(mailService, times(1)).sendPasswordResetSuccessMessage(email);
        assertNotNull(getAccessToken(email, newPassword));
    }

    @Test
    @DirtiesContext
    public void shouldUpdateInfo_byAuthenticated() {
        final String info = "New info";
        final UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setInfo(info);
        final ResponseEntity<Response<Void>> response = restTemplate.exchange("/users/update-info",
            HttpMethod.POST,
            prepareHttpEntity(request, getSingleUserAccessToken()),
            new ParameterizedTypeReference<Response<Void>>() {
            });
        final Response<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertEquals("200", body.getCode());
        assertEquals(info, userRepository.getById(1000002L).getInfo());
    }
}