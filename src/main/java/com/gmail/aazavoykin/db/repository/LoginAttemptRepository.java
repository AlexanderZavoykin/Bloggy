package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.LoginAttempt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends CrudRepository<LoginAttempt, Long> {

    @Query(value = "select count(a) from login_attempt a where a.attempted > now() - interval '30 mins' and a.success = false",
            nativeQuery = true)
    int countLoginAttemptsLast30Mins();

}
