package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.Story;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends CrudRepository<Story, Long> {

    Story getById(Long id);

    @Query(value = "select s from Story s")
    List<Story> all();

    @Query(value = "select s from Story s where s.user.id = :userId")
    List<Story> getAllByUser(@Param("userId") Long userId);

    List<Story> getFirst10ByOrderByCreated();

    @Query("select s from Story s join Tag t where t.name = :tagName")
    List<Story> getAllByTagName(@Param("tagName") String tagName);


}
