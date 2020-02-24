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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
                new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
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

    public StoryDto save(StoryDto dto) {
        final User user = checkAuthorized();
        final Story story = new Story();
        story.setUser(user);
        story.setTitle(dto.getTitle());
        story.setBody(dto.getBody());
        story.setTags(dto.getTags().stream().map(Tag::new).collect(Collectors.toList()));
        final Story saved = storyRepository.save(story);
        log.debug("Saving new story {}", saved);
        return storyMapper.storyToStoryDto(saved);
    }

    public StoryDto update(StoryDto dto) {
        final User user = checkAuthorized();
        checkAvailibleForUser(user, dto.getId());
        final Story found = Optional.ofNullable(storyRepository.getById(dto.getId()))
                .orElseThrow(() -> new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
        found.setTitle(dto.getTitle());
        found.setBody(dto.getBody());
        found.setTags(dto.getTags().stream().map(Tag::new).collect(Collectors.toList()));
        log.debug("Updating new story {}", found);
        return storyMapper.storyToStoryDto(storyRepository.save(found));
    }

    public void delete(Long id) {
        final User user = checkAuthorized();
        checkAvailibleForUser(user, id);
        log.debug("Deleting story {}", id);
        storyRepository.deleteById(id);
    }

    private User checkAuthorized() {
        final UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable(userRepository.getByEmail(principal.getUsername())).orElseThrow(() -> {
            log.error("Tried to operate story by unauthorized user");
            throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
        });
    }

    private void checkAvailibleForUser(User user, Long storyId) {
        if (storyRepository.getStoryIdsByUserId(user.getId()).stream().noneMatch(id -> id.equals(storyId))) {
            log.error("Tried to operate story {} by another user {}", storyId, user);
            throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
        }
    }

}
