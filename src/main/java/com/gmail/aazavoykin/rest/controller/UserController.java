package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.service.StoryService;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final StoryService storyService;

    @Autowired
    private final UserService userService;

    @GetMapping(value = "/user/")
    public ModelAndView all() {
        final ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("users", userService.getAll());
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping(value = "/user/{userId}")
    public ModelAndView userInfo(@PathVariable("userId") Long userId) {
        final ModelAndView modelAndView = new ModelAndView("user");
        modelAndView.addObject("user", userService.getById(userId));
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping(value = "/user/{userId}/stories/")
    public ModelAndView userStories(@PathVariable("userId") Long userId) {
        final List<StoryDto> storyDtos = userService.getById(userId).getStories().stream()
                .map((story) -> new StoryDto(story, 50))
                .collect(Collectors.toList());
        final ModelAndView modelAndView = new ModelAndView("stories");
        modelAndView.addObject("stories", storyDtos);
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping(value = "/user/{userId}/comments/")
    public ModelAndView userComments(@PathVariable("userId") Long userId) {
        final ModelAndView modelAndView = new ModelAndView("comments");
        modelAndView.addObject("comments", userService.getById(userId).getComments());
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

}
