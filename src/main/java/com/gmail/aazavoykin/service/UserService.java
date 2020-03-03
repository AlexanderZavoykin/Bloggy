package com.gmail.aazavoykin.service;

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
import com.gmail.aazavoykin.rest.request.UserSignupRequest;
import com.gmail.aazavoykin.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final UserTokenRepository tokenRepository;

    private final LoginAttemptRepository loginAttemptRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return Optional.ofNullable(userRepository.getByEmail(email)).orElseThrow(() ->
                new InternalException(InternalErrorType.USER_NOT_FOUND));
    }

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
        final UserToken token = new UserToken()
                .setUser(savedUser)
                .setToken(UUID.randomUUID().toString());
        tokenRepository.save(token);
        // TODO add service to generate activation url and send it to user`s email
    }

    public void activate(String token) {
        final UserToken foundToken = Optional.ofNullable(tokenRepository.findByToken(token))
                .orElseThrow(() -> new InternalException(InternalErrorType.TOKEN_NOT_FOUND));
        Optional.of(foundToken).filter(userToken -> LocalDate.now().isBefore(userToken.getExpiryDate()))
                .orElseThrow(() -> new InternalException(InternalErrorType.TOKEN_DATE_EXPIRED));
        final User user = foundToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void setEnabled(User user, boolean enabled) {
        userRepository.save(user.setEnabled(enabled));
    }

    public void updateInfo(Principal principal, String info) {
        userRepository.updateInfo(((User) principal).getId(), info);
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
