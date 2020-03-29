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

    List<Story> getAllByRoughFalseOrderByCreatedDesc();

    List<Story> getTop10ByRoughFalseOrderByCreatedDesc();

    @Query("select s from Story s join Tag t where s.rough = false and t.name = :tagName")
    List<Story> getAllByTagName(@Param("tagName") String tagName);

    List<Story> getAllByUserNicknameAndRoughFalse(String nickname);

    List<Story> getAllByUserNicknameAndRoughTrue(String nickname);
}
