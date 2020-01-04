package com.gmail.aazavoykin.service;

import com.gmail.aazavoykin.exception.EntityNotFoundException;
import com.gmail.aazavoykin.model.Story;
import com.gmail.aazavoykin.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class StoryService {

    @Autowired
    private final StoryRepository storyRepository;

    public List<Story> getAll() {
        return (List<Story>) storyRepository.findAll();
    }

    public List<Story> getAllByUserId(Long userId) {
        return storyRepository.getAllByUser(userId);
    }

    public Story getById(Long id) {
        return storyRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Story with id=" + id + " not found"));
    }

    public Story save(Story story) {
        return storyRepository.save(story);
    }

    public void delete(Long id) {
        storyRepository.deleteById(id);
    }

}
