package com.gmail.aazavoykin.repository;

import com.gmail.aazavoykin.model.Story;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends CrudRepository<Story, Long> {

    @Query("select s from Story s where s.user.id = :userId")
    List<Story> getAllByUser(@Param("userId") Long userId);

}
