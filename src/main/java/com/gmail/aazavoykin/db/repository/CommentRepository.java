package com.gmail.aazavoykin.db.repository;

import com.gmail.aazavoykin.db.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> getAllByUserNicknameIgnoreCase(String nickname);
}
