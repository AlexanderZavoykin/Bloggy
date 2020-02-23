package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.repository.UserRepository;
import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder encoder;

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
    }

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
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    private boolean nicknameExists(String username) {
        return userRepository.findByNickname(username) != null;
    }

}
