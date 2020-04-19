package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Story getById(Long id);
    List<Story> getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc();
    List<Story> getTop10ByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc();
    List<Story> getAllByRoughFalseAndTags_NameIgnoreCaseAndUser_EnabledTrue(String tagName);
    List<Story> getAllByUserNicknameIgnoreCaseAndRoughFalse(String nickname);
    List<Story> getAllByUserNicknameIgnoreCase(String nickname);
}
