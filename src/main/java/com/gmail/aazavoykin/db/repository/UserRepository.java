package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> getAllByOrderByNickname();
    User getById(Long id);
    User getByNicknameIgnoreCase(String username);
    User getByEmailIgnoreCase(String email);
}

