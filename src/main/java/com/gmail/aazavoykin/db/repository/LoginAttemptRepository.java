package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.LoginAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, Long> {

    @Query(value = "select count(a) from login_attempt a " +
            "where a.attempted > now() - interval '30 mins' " +
            "and a.success = false " +
            "and a.user_id = :userId",
            nativeQuery = true)
    Long countFailedLoginAttemptsLast30Mins(@Param("userId") Long userId);

}
