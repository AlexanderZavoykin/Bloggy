package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.service.CommentService;
import com.gmail.aazavoykin.service.StoryService;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final StoryService storyService;

    private final CommentService commentService;

    @GetMapping(value = "/users")
    public List<UserDto> all() {
        return userService.getAll();
    }

    @GetMapping(value = "/{nickname}")
    public UserDto userInfo(@PathVariable("nickname") String nickname) {
        return userService.getByNickname(nickname);
    }

    @GetMapping("/{nickname}/stories")
    public List<StoryDto> getStoriesByAuthorNickname(@PathVariable("nickname") String nickname) {
        return storyService.getAllByUserNickname(nickname);
    }

    @GetMapping("/{nickname}/stories/rough")
    public List<StoryDto> getRoughStoriesByAuthorNickname(@PathVariable("nickname") String nickname) {
        return storyService.getRoughByUserNickname(nickname);
    }

    @GetMapping("/{nickname}/comments")
    public List<CommentDto> getCommentsByAuthorNickname(@PathVariable("nickname") String nickname) {
        return commentService.getAllByAuthorNickname(nickname);
    }

}
