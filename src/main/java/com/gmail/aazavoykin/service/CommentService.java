package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.repository.CommentRepository;
import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.mapper.CommentMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public List<CommentDto> getAllByAuthorNickname(String nickname) {
        return commentMapper.commentsToCommentDtos(commentRepository.getAllByUserNickname(nickname));
    }
}

