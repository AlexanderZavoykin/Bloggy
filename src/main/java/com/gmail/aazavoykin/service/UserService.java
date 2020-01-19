package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.exception.EntityAlreadyExistsException;
import com.gmail.aazavoykin.exception.EntityNotFoundException;
import com.gmail.aazavoykin.model.Role;
import com.gmail.aazavoykin.model.User;
import com.gmail.aazavoykin.repository.UserRepository;
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
                new EntityNotFoundException("User with id=" + id + " not found"));
    }

    @Transactional
    public User save(UserDto userDto) {
        final String email = userDto.getEmail().toLowerCase();
        final String username = userDto.getUsername().toLowerCase();
        final String nickname = userDto.getNickname().toLowerCase();
        if (emailExists(email)) {
            throw new EntityAlreadyExistsException("There is already an account with that email: " + email);
        }
        if (usernameExists(username)) {
            throw new EntityAlreadyExistsException("There is already an account with that username: " + username);
        }
        if (nicknameExists(nickname)) {
            throw new EntityAlreadyExistsException("There is already an account with that nickname: " + nickname);
        }
        final User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setNickname(nickname);
        user.setEmail(email);
        user.setInfo(userDto.getInfo());
        user.setRole(Role.USER);
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
