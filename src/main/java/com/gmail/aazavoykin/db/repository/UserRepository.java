package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> getAllByOrderByNickname();

    User getById(Long id);

    User getByNickname(String username);

    User getByEmail(String email);

    @Query("update User u set u.info = :info where u.id = :userId")
    void updateInfo(@Param("userId")Long userId, @Param("info") String info);

}

