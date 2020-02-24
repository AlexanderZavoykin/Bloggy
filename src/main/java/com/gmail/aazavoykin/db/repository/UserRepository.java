package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> getAllByOrderByNickname();

    User getByNickname(String username);

    User getByEmail(String email);

}

