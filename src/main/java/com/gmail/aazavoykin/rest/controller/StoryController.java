package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.model.Story;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StoryController {

    @Autowired
    private final StoryService storyService;

    @GetMapping(value = "/")
    public ModelAndView all() {
        final List<StoryDto> storyDtos = storyService.getAll().stream()
                .map((story) -> new StoryDto(story, 50))
                .collect(Collectors.toList());
        final ModelAndView modelAndView = new ModelAndView("stories");
        modelAndView.addObject("stories", storyDtos);
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping(value = "/story/{storyId}")
    public ModelAndView getById(@PathVariable("storyId") Long storyId) {
        final Story story = storyService.getById(storyId);
        final ModelAndView modelAndView = new ModelAndView("story");
        modelAndView.addObject("story", new StoryDto(story, 0));
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/story")
    public ModelAndView create(@Valid @RequestBody StoryDto request) {
        final Story story = new Story();
        story.setTitle(request.getTitle());
        story.setBody(request.getBody());
        story.setTags(request.getTags());
        story.setCreated(LocalDateTime.now());
        // TODO get current user
        final Story publicated = storyService.save(story);
        final ModelAndView modelAndView = new ModelAndView("story");
        modelAndView.addObject("story", new StoryDto(publicated, 0));
        modelAndView.setStatus(HttpStatus.CREATED);
        return modelAndView;
    }

}
