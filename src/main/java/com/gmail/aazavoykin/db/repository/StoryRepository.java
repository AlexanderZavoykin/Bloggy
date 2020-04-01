package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Story getById(Long id);
    List<Story> getAllByRoughFalseOrderByCreatedDesc();
    List<Story> getTop10ByRoughFalseOrderByCreatedDesc();
    List<Story> getAllByTags_Name( String tagName);
    List<Story> getAllByUserNicknameAndRoughFalse(String nickname);
    List<Story> getAllByUserNicknameAndRoughTrue(String nickname);
}
