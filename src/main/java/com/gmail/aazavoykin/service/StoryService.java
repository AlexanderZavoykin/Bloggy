package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.db.model.Tag;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.model.enums.Role;
import com.gmail.aazavoykin.db.repository.StoryRepository;
import com.gmail.aazavoykin.db.repository.TagRepository;
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
    private final TagRepository tagRepository;

    /*
     * If tagName is presented then returns DTOs filtered by it,
     * else returns all not-rough stories` DTOs
     */
    public List<StoryDto> getAll(String tagName) {
        final List<Story> stories = Optional.ofNullable(tagName)
            .map(tn -> storyRepository.getAllByTags_NameIgnoreCase(tagName))
            .orElse(storyRepository.getAllByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc());
        return storyMapper.storiesToStoryDtos(stories);
    }

    public List<StoryDto> getLast10() {
        return storyMapper.storiesToStoryDtos(storyRepository.getTop10ByRoughFalseAndUser_EnabledTrueOrderByCreatedDesc());
    }

    /*
     * If requested story is rough then returns it only for its author
     */
    public StoryDto getById(Long id) {
        final Story story = storyRepository.findById(id)
            .orElseThrow(() -> new InternalException(InternalErrorType.STORY_NOT_FOUND));
        if (story.isRough()) {
            final User user = AppUser.getCurrentUser()
                .map(appUser -> userRepository.getById(appUser.getId()))
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            if (!story.getUser().equals(user) || !user.isEnabled()) {
                throw new InternalException(InternalErrorType.STORY_NOT_FOUND);
            }
        }
        return storyMapper.storyToStoryDto(story);
    }

    /*
     * If requested by user with its own nickname then returns all stories including the rough ones
     * If user found by this nickname is disabled, returns its not-rough stories` DTOs only for Admin
     * Else returns all not-rough stories` DTOs for this user
     */
    public List<StoryDto> getAllByUserNickname(String nickname) {
        final User user = Optional.ofNullable(userRepository.getByNicknameIgnoreCase(nickname))
            .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
        final Optional<AppUser> optionalAppUser = AppUser.getCurrentUser();
        boolean isHimself = optionalAppUser.map(appUser -> {
                final User authenticated = userRepository.getById(appUser.getId());
                return authenticated.equals(user);
            }
        ).orElse(false);
        boolean isAdmin = optionalAppUser.map(appUser -> appUser.hasRole(Role.ADMIN)).orElse(false);
        final List<Story> stories;
        if (isHimself) {
            stories = storyRepository.getAllByUserNicknameIgnoreCase(nickname);
        } else if (user.isEnabled() || isAdmin) {
            stories = storyRepository.getAllByUserNicknameIgnoreCaseAndRoughFalse(nickname);
        } else {
            throw new InternalException(InternalErrorType.USER_NOT_FOUND);
        }
        return storyMapper.storiesToStoryDtos(stories);
    }

    @Transactional
    public StoryDto save(StorySaveRequest request) {
        return AppUser.getCurrentUser().map(appUser -> {
            final User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            if (!user.getRoles().contains(Role.USER)) {
                log.debug("Tried to save a story by user with id={} who has no USER role", user.getId());
                throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
            }
            final Story story = new Story();
            story.setUser(user);
            story.setTitle(request.getTitle());
            story.setBody(request.getBody());
            final List<Tag> requestTags = Optional.ofNullable(request.getTagNames())
                .map(tagNames -> tagNames.stream()
                    .map(tagName -> Optional.ofNullable(tagRepository.getByNameIgnoreCase(tagName)).orElse(new Tag(tagName)))
                    .collect(Collectors.toList()))
                .orElse(null);
            story.setTags(requestTags);
            final Story saved = storyRepository.saveAndFlush(story);
            log.debug("Saved new story {}", saved);
            return storyMapper.storyToStoryDto(saved);
        }).orElse(null);
    }

    @Transactional
    public StoryDto update(Long storyId, StoryUpdateRequest request) {
        return AppUser.getCurrentUser().map(appUser -> {
            final User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            final Story found = Optional.ofNullable(storyRepository.getById(storyId))
                .orElseThrow(() -> new InternalException(InternalErrorType.STORY_NOT_FOUND));
            checkAvailibleForUser(user, storyId);
            found.setTitle(request.getTitle());
            found.setBody(request.getBody());
            found.setRough(request.isRough());
            final List<Tag> requestTags = Optional.ofNullable(request.getTagNames())
                .map(tagNames -> tagNames.stream()
                    .map(tagName -> Optional.ofNullable(tagRepository.getByNameIgnoreCase(tagName)).orElse(new Tag(tagName)))
                    .collect(Collectors.toList()))
                .orElse(null);
            found.setTags(requestTags);
            final Story updated = storyRepository.saveAndFlush(found);
            tagRepository.removeOrphans();
            log.debug("Updated story {}", updated);
            return storyMapper.storyToStoryDto(updated);
        }).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        AppUser.getCurrentUser().ifPresent(appUser -> {
            final User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new InternalException(InternalErrorType.USER_NOT_FOUND));
            checkAvailibleForUser(user, id);
            log.debug("Deleting story {}", id);
            storyRepository.deleteById(id);
            tagRepository.removeOrphans();
        });
    }

    private void checkAvailibleForUser(User user, Long storyId) {
        if (!storyRepository.getById(storyId).getUser().equals(user)) {
            log.error("Tried to operate story {} by another user {}", storyId, user);
            throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
        }
    }
}
