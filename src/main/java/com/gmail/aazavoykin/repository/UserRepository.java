package com.gmail.aazavoykin.repository;

import com.gmail.aazavoykin.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByNickname(String username);

}

