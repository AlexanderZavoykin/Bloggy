package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.db.model.Tag;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.repository.StoryRepository;
import com.gmail.aazavoykin.db.repository.UserRepository;
import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.mapper.StoryMapper;
import com.gmail.aazavoykin.rest.request.StorySaveRequest;
import com.gmail.aazavoykin.rest.request.StoryUpdateRequest;
import com.gmail.aazavoykin.security.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class StoryService {

    private final StoryMapper storyMapper;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    public List<StoryDto> getAll() {
        return storyMapper.storiesToStoryDtos(storyRepository.getAllByRoughFalseOrderByCreatedDesc());
    }

    public List<StoryDto> getLast10() {
        return storyMapper.storiesToStoryDtos(storyRepository.getTop10ByRoughFalseOrderByCreatedDesc());
    }

    public StoryDto getById(Long id) {
        final Story story = storyRepository.findById(id).orElseThrow(() ->
            new InternalException(InternalErrorType.STORY_NOT_FOUND));
        return storyMapper.storyToStoryDto(story);
    }

    public List<StoryDto> getAlByTag(String tagName) {
        return storyMapper.storiesToStoryDtos(storyRepository.getAllByTagName(tagName));
    }

    public List<StoryDto> getAllByUserNickname(String nickname) {
        return storyMapper.storiesToStoryDtos(storyRepository.getAllByUserNicknameAndRoughFalse(nickname));
    }

    public List<StoryDto> getRoughByUserNickname(String nickname) {
        return storyMapper.storiesToStoryDtos(storyRepository.getAllByUserNicknameAndRoughTrue(nickname));
    }

    @Transactional
    public StoryDto save(StorySaveRequest request) {
        return AppUser.getCurrentUser().map(appUser -> {
            final User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            final Story story = new Story();
            story.setUser(user);
            story.setTitle(request.getTitle());
            story.setBody(request.getBody());
            story.setTags(Optional.ofNullable(request.getTagNames())
                .map(tags -> tags.stream().map(Tag::new).collect(Collectors.toList()))
                .orElse(null));
            log.debug("Saving new story {}", story);
            final Story saved = storyRepository.save(story);
            log.debug("Saved new story {}", saved);
            return storyMapper.storyToStoryDto(saved);
        }).orElseThrow(() -> new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE));
    }

    @Transactional
    public StoryDto update(StoryUpdateRequest request) {
        return AppUser.getCurrentUser().map(appUser -> {
            final User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            checkAvailibleForUser(user, request.getId());
            final Story found = Optional.ofNullable(storyRepository.getById(request.getId()))
                .orElseThrow(() -> new InternalException(InternalErrorType.STORY_NOT_FOUND));
            found.setTitle(request.getTitle());
            found.setBody(request.getBody());
            final List<Tag> requestTags = Optional.ofNullable(request.getTagNames())
                .map(tagNames -> tagNames.stream().map(Tag::new).collect(Collectors.toList()))
                .orElse(null);
            found.setTags(requestTags);
            log.debug("Updating new story {}", found);
            return storyMapper.storyToStoryDto(found);
        }).orElseThrow(() -> new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE));
    }

    public void delete(Long id) {
        final AppUser appUser = AppUser.getCurrentUser()
            .orElseThrow(() -> new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE));
        final User user = userRepository.findById(appUser.getId())
            .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
        checkAvailibleForUser(user, id);
        log.debug("Deleting story {}", id);
        storyRepository.deleteById(id);
    }

    private void checkAvailibleForUser(User user, Long storyId) {
        if (!storyRepository.getById(storyId).getUser().equals(user)) {
            log.error("Tried to operate story {} by another user {}", storyId, user);
            throw new InternalException(InternalErrorType.ACCESS_DENIED);
        }
    }
}
