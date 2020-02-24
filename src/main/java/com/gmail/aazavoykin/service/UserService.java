package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.repository.UserRepository;
import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.rest.dto.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return Optional.ofNullable(userRepository.getByEmail(email)).orElseThrow(() ->
                new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
    }

    public List<UserDto> getAll() {
        return userMapper.usersToUserDtos(userRepository.getAllOrderByNickname());
    }

    public UserDto getByNickname(String nickname) {
        final User user = Optional.ofNullable(userRepository.getByNickname(nickname)).orElseThrow(() ->
                new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
        return userMapper.userToUserDto(user);
    }


    // TODO check all below:

    @Transactional
    public User save(UserDto userDto) {
        final String email = userDto.getEmail().toLowerCase();
        final String nickname = userDto.getNickname().toLowerCase();
        if (emailExists(email) || nicknameExists(nickname)) {
            throw new InternalException(InternalErrorType.ENTITY_ALREADY_EXISTS);
        }
        final User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setInfo(userDto.getInfo());
        //user.setRole(RoleName.USER);
        user.setStories(null);
        user.setComments(null);
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.getByEmail(email) != null;
    }

    private boolean nicknameExists(String username) {
        return userRepository.getByNickname(username) != null;
    }

}
