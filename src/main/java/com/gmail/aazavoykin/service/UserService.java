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
import com.gmail.aazavoykin.rest.dto.mapper.ExtendedUserMapper;
import com.gmail.aazavoykin.rest.dto.mapper.UserMapper;
import com.gmail.aazavoykin.rest.request.ChangePasswordRequest;
import com.gmail.aazavoykin.rest.request.UpdateUserInfoRequest;
import com.gmail.aazavoykin.rest.request.UserActivationRequest;
import com.gmail.aazavoykin.rest.request.UserSignupRequest;
import com.gmail.aazavoykin.security.AppUser;
import com.gmail.aazavoykin.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private static final String HIDDEN_EMAIL = "NOT_AVAILABLE";
    private final AppProperties appProperties;
    private final UserMapper userMapper;
    private final ExtendedUserMapper extendedUserMapper;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserTokenRepository tokenRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordEncoder encoder;

    private static void checkExpired(UserToken token) {
        if (!LocalDateTime.now().isBefore(token.getExpiryDate())) {
            throw new InternalException(InternalErrorType.TOKEN_DATE_EXPIRED);
        }
    }

    /*
     * If requested by Admin returns extended DTOs for found users,
     * else returns simple DTOs with hidden e-mails only for enabled (activated) users who have a USER role
     * (pure ADMINs will not be showed)
     */
    public List<? extends UserDto> getAll() {
        final List<User> users = userRepository.getAllByOrderByNickname();
        if (AppUser.getCurrentUser().filter(appUser -> appUser.hasRole(Role.ADMIN)).isPresent()) {
            return extendedUserMapper.usersToExtendedUserDtos(users);
        } else {
            return users.stream()
                .filter(user -> user.isEnabled() && user.getRoles().contains(Role.USER))
                .map(user -> hideEmail(userMapper.userToUserDto(user)))
                .collect(Collectors.toList());
        }
    }

    public User getByEmail(String email) {
        return userRepository.getByEmailIgnoreCase(email);
    }

    /*
     * If requested by Admin returns extended DTO for a found user,
     * else returns only enabled (activated) user`s simple DTO and hide e-mail
     */
    public UserDto getByNickname(String nickname) {
        final User user = Optional.ofNullable(userRepository.getByNicknameIgnoreCase(nickname)).orElseThrow(() ->
            new InternalException(InternalErrorType.USER_NOT_FOUND));
        if (AppUser.getCurrentUser().filter(appUser -> appUser.hasRole(Role.ADMIN)).isPresent()) {
            return extendedUserMapper.userToExtendedUserDto(user);
        } else if (user.isEnabled() && user.getRoles().contains(Role.USER)) {
            return hideEmail(userMapper.userToUserDto(user));
        } else {
            throw new InternalException(InternalErrorType.USER_NOT_FOUND);
        }
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
            .setToken(UUID.randomUUID().toString())
            .setExpiryDate(LocalDateTime.now().plus(appProperties.getAuth().getSignup().getLifetime(), ChronoUnit.DAYS)));
        mailService.sendActivationUrl(savedUser.getEmail(), savedToken.getToken());
    }

    @Transactional
    public void activate(String token) {
        final UserToken foundToken = Optional.ofNullable(tokenRepository.findByToken(token))
            .orElseThrow(() -> new InternalException(InternalErrorType.TOKEN_NOT_FOUND));
        checkExpired(foundToken);
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
        final User user = Optional.ofNullable(userRepository.getByEmailIgnoreCase(email))
            .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
        final UserToken token = Optional.ofNullable(tokenRepository.findByUserId(user.getId()))
            .orElseGet(() -> {
                final UserToken newToken = new UserToken();
                newToken.setUser(user);
                return newToken;
            })
            .setExpiryDate(LocalDateTime.now().plus(appProperties.getAuth().getForgotPassword().getLifetime(), ChronoUnit.DAYS))
            .setToken(UUID.randomUUID().toString());
        tokenRepository.saveAndFlush(token);
        mailService.sendPasswordResetUrl(email, token.getToken());
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, String token) {
        final UserToken foundToken = Optional.ofNullable(tokenRepository.findByToken(token))
            .orElseThrow(() -> new InternalException(InternalErrorType.TOKEN_NOT_FOUND));
        checkExpired(foundToken);
        ValidationUtils.checkMatchingPassword(request.getPassword(), request.getMatchingPassword());
        final User user = foundToken.getUser();
        final String encodedPassword = encoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        mailService.sendPasswordResetSuccessMessage(user.getEmail());
    }

    @Transactional
    public void setEnabled(Long id, boolean enabled) {
        userRepository.getById(id).setEnabled(enabled);
    }

    @Transactional
    public void updateInfo(UpdateUserInfoRequest request) {
        AppUser.getCurrentUser().ifPresent(appUser -> {
            final User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            user.setInfo(request.getInfo());
        });
    }

    @Transactional
    public void enable(String nickname, UserActivationRequest request) {
        AppUser.getCurrentUser().ifPresent(appUser -> {
            if (!appUser.hasRole(Role.ADMIN)) {
                throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
            }
            final User user = Optional.ofNullable(userRepository.getByNicknameIgnoreCase(nickname))
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            setEnabled(user.getId(), request.isEnabled());
        });
    }

    public Long insertLoginAttempt(Long userId, boolean success) {
        loginAttemptRepository.save(new LoginAttempt()
            .setUser(userRepository.getById(userId))
            .setSuccess(success));
        return loginAttemptRepository.countFailedLoginAttemptsLast30Mins(userId);
    }

    private void checkEmailNotExists(String email) {
        if (userRepository.getByEmailIgnoreCase(email) != null) {
            throw new InternalException(InternalErrorType.USER_ALREADY_EXISTS);
        }
    }

    private void checkNicknameNotExists(String nickname) {
        if (userRepository.getByNicknameIgnoreCase(nickname) != null) {
            throw new InternalException(InternalErrorType.USER_ALREADY_EXISTS);
        }
    }

    private UserDto hideEmail(UserDto userDto) {
        final boolean isAdmin = AppUser.getCurrentUser().map(appUser -> appUser.hasRole(Role.ADMIN)).orElse(false);
        if (!isAdmin) {
            userDto.setEmail(HIDDEN_EMAIL);
        }
        return userDto;
    }
}
