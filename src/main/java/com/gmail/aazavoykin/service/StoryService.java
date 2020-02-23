package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.db.model.Tag;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.db.repository.StoryRepository;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.mapper.StoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class StoryService {

    @Autowired
    private final StoryRepository storyRepository;

    public List<StoryDto> getAll() {
        return StoryMapper.INSTANCE.storiesToStoryDtos(storyRepository.all());
    }

    public List<StoryDto> getLast10() {
        return StoryMapper.INSTANCE.storiesToStoryDtos(storyRepository.getFirst10ByOrderByCreated());
    }

    public StoryDto getById(Long id) {
        final Story story = storyRepository.findById(id).orElseThrow(() ->
                new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
        return StoryMapper.INSTANCE.storyToStoryDto(story);
    }

    public List<StoryDto> getAlByTag(String tagName) {
        return StoryMapper.INSTANCE.storiesToStoryDtos(storyRepository.getAllByTagName(tagName));
    }

    public List<Story> getAllByUserId(Long userId) {
        return storyRepository.getAllByUser(userId);
    }

    public Story save(StoryDto dto) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final Story story = new Story();
        story.setUser(user);
        story.setTitle(dto.getTitle());
        story.setBody(dto.getBody());
        story.setTags(dto.getTags().stream().map(Tag::new).collect(Collectors.toList()));
        return storyRepository.save(story);
    }

    public Story update(StoryDto dto) {
        checkIsAvailibleForUser(dto.getId());
        final Story found = Optional.ofNullable(storyRepository.getById(dto.getId()))
                .orElseThrow(() -> new InternalException(InternalErrorType.ENTITY_NOT_FOUND));
        found.setTitle(dto.getTitle());
        found.setBody(dto.getBody());
        found.setTags(dto.getTags().stream().map(Tag::new).collect(Collectors.toList()));
        return storyRepository.save(found);
    }

    public void delete(Long id) {
        checkIsAvailibleForUser(id);
        storyRepository.deleteById(id);
    }

    private void checkIsAvailibleForUser(Long storyId) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final boolean ofUser = storyRepository.getAllByUser(user.getId()).stream()
                .map(Story::getId)
                .anyMatch(id -> id.equals(storyId));
        if (!ofUser) {
            throw new InternalException(InternalErrorType.OPERATION_NOT_AVAILABLE);
        }
    }

}
