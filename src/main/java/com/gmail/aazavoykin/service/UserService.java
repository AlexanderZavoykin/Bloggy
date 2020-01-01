package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.model.User;
import com.gmail.aazavoykin.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

}
