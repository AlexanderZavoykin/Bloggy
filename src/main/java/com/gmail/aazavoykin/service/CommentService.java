package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.model.Comment;
import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.repository.CommentRepository;
import com.gmail.aazavoykin.db.repository.StoryRepository;
import com.gmail.aazavoykin.db.repository.UserRepository;
import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.mapper.CommentMapper;
import com.gmail.aazavoykin.security.AppUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Service
public class CommentService {

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final StoryService storyService;

    public List<CommentDto> getAllByAuthorNickname(String nickname) {
        return commentMapper.commentsToCommentDtos(commentRepository.getAllByUserNickname(nickname));
    }

    @Transactional
    public void addComment(Long storyId, String commentBody) {
        final AppUser appUser = AppUser.getCurrentUser()
            .orElseThrow(() -> new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE));
        final User user = userRepository.findById(appUser.getId())
            .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
        final Story story = Optional.ofNullable(storyRepository.getById(storyId))
            .orElseThrow(() -> new InternalException(InternalErrorType.STORY_NOT_FOUND));
        story.getComments().add(new Comment()
            .setBody(commentBody)
            .setUser(user));
    }
}

