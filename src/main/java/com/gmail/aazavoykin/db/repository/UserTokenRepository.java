package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    UserToken findByToken(String token);
    UserToken findByUserId(Long id);
}
