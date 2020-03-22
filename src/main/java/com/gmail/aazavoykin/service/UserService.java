package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.configuration.properties.AppProperties;
import com.gmail.aazavoykin.db.model.LoginAttempt;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.model.UserToken;
import com.gmail.aazavoykin.db.model.enums.Role;
import com.gmail.aazavoykin.db.repository.LoginAttemptRepository;
import com.gmail.aazavoykin.db.repository.UserRepository;
import com.gmail.aazavoykin.db.repository.UserTokenRepository;
import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.rest.dto.mapper.UserMapper;
import com.gmail.aazavoykin.rest.request.ResetPasswordRequest;
import com.gmail.aazavoykin.rest.request.UserSignupRequest;
import com.gmail.aazavoykin.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final AppProperties appProperties;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserTokenRepository tokenRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder encoder;

    public List<UserDto> getAll() {
        return userMapper.usersToUserDtos(userRepository.getAllByOrderByNickname());
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public UserDto getByNickname(String nickname) {
        final User user = Optional.ofNullable(userRepository.getByNickname(nickname)).orElseThrow(() ->
            new InternalException(InternalErrorType.USER_NOT_FOUND));
        return userMapper.userToUserDto(user);
    }

    @Transactional
    public void add(UserSignupRequest request) {
        final String email = request.getEmail();
        final String nickname = request.getNickname();
        ValidationUtils.checkMatchingPassword(request.getPassword(), request.getMatchingPassword());
        checkEmailNotExists(email);
        checkNicknameNotExists(nickname);
        final User user = new User()
            .setStories(new ArrayList<>())
            .setComments(new ArrayList<>())
            .setRoles(new HashSet<>())
            .setEmail(email)
            .setPassword(encoder.encode(request.getPassword()))
            .setNickname(nickname)
            .setInfo("A newbie")
            .setEnabled(false);
        user.getRoles().add(Role.USER);
        final User savedUser = userRepository.save(user);
        final UserToken savedToken = tokenRepository.save(new UserToken()
            .setUser(savedUser)
            .setToken(UUID.randomUUID().toString()))
            .setExpiryDate(LocalDateTime.now().plus(appProperties.getAuth().getSignup().getLifetime(), ChronoUnit.DAYS));
        // TODO mailService.sendVerificationUrl(savedUser.getEmail(), savedToken.getToken());
    }

    @Transactional
    public void activate(String token) {
        final UserToken foundToken = Optional.ofNullable(tokenRepository.findByToken(token))
            .orElseThrow(() -> new InternalException(InternalErrorType.TOKEN_NOT_FOUND));
        Optional.of(foundToken).filter(userToken -> LocalDateTime.now().isBefore(userToken.getExpiryDate()))
            .orElseThrow(() -> new InternalException(InternalErrorType.TOKEN_DATE_EXPIRED));
        final User user = foundToken.getUser();
        if (!user.isEnabled()) {
            user.setEnabled(true);
        } else {
            throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
        }
        mailService.sendSuccessfulRegistrationConfirmation(user.getEmail());
    }

    @Transactional
    public void sendResetPasswordUrl(String email) {
        final User user = Optional.ofNullable(userRepository.getByEmail(email))
            .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
        final UserToken token = Optional.ofNullable(tokenRepository.findByUser(user.getId()))
            .orElseGet(() -> tokenRepository.save(new UserToken()
                .setUser(user)));
        token.setToken(UUID.randomUUID().toString());
        mailService.sendPasswordResetUrl(email, token.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request, String email) {
        final String newPassword = request.getPassword();
        final User user = Optional.ofNullable(userRepository.getByEmail(email))
            .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
        ValidationUtils.checkMatchingPassword(request.getPassword(), request.getMatchingPassword());
        user.setPassword(newPassword);
        mailService.sendPasswordResetSuccessMessage(email);
    }

    @Transactional
    public void setEnabled(Long id, boolean enabled) {
        userRepository.getById(id).setEnabled(enabled);
    }

    @Transactional
    public void updateInfo(Principal principal, String info) {
        final User user = userRepository.getById(((User) principal).getId());
        user.setInfo(info);
    }

    public Long insertLoginAttempt(Long userId, boolean success) {
        loginAttemptRepository.save(new LoginAttempt()
            .setUser(userRepository.getById(userId))
            .setSuccess(success));
        return loginAttemptRepository.countFailedLoginAttemptsLast30Mins(userId);
    }

    private void checkEmailNotExists(String email) {
        if (userRepository.getByEmail(email) != null) {
            throw new InternalException(InternalErrorType.USER_ALREADY_EXISTS);
        }
    }

    private void checkNicknameNotExists(String nickname) {
        if (userRepository.getByNickname(nickname) != null) {
            throw new InternalException(InternalErrorType.USER_ALREADY_EXISTS);
        }
    }
}
