package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.model.enums.Role;
import com.gmail.aazavoykin.db.repository.UserRepository;
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

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return Optional.ofNullable(userRepository.getByEmail(email)).orElseThrow(() ->
                new InternalException(InternalErrorType.USER_NOT_FOUND));
    }

    public List<UserDto> getAll() {
        return userMapper.usersToUserDtos(userRepository.getAllByOrderByNickname());
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
        checkEmailExists(email);
        checkNicknameExists(nickname);
        final User user = new User()
                .setStories(new ArrayList<>())
                .setComments(new ArrayList<>())
                .setRoles(Set.of(Role.USER))
                .setEmail(email)
                .setPassword(encoder.encode(request.getPassword()))
                .setNickname(nickname)
                .setInfo("A newbie")
                .setEnabled(true); // change to false when use email verification
        userRepository.save(user);
    }

    public void checkEmailExists(String email) {
        Optional.ofNullable(userRepository.getByEmail(email)).orElseThrow(() ->
                new InternalException(InternalErrorType.USER_ALREADY_EXISTS));
    }

    public void checkNicknameExists(String nickname) {
        Optional.ofNullable(userRepository.getByNickname(nickname)).orElseThrow(() ->
                new InternalException(InternalErrorType.USER_ALREADY_EXISTS));
    }

}
