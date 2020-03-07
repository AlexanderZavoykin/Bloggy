package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.rest.request.ResetPasswordRequest;
import com.gmail.aazavoykin.rest.request.UserLoginRequest;
import com.gmail.aazavoykin.rest.request.UserSignupRequest;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.CommentService;
import com.gmail.aazavoykin.service.StoryService;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    private final StoryService storyService;

    private final CommentService commentService;

    @GetMapping(value = "users")
    public Response<List<UserDto>> all() {
        return Response.success(userService.getAll());
    }

    @GetMapping(value = "{nickname}")
    public Response<UserDto> userInfo(@PathVariable("nickname") String nickname) {
        return Response.success(userService.getByNickname(nickname));
    }

    @GetMapping("{nickname}/stories")
    public Response<List<StoryDto>> getStoriesByAuthorNickname(@PathVariable("nickname") String nickname) {
        return Response.success(storyService.getAllByUserNickname(nickname));
    }

    @GetMapping("{nickname}/stories/rough")
    public Response<List<StoryDto>> getRoughStoriesByAuthorNickname(@PathVariable("nickname") String nickname) {
        return Response.success(storyService.getRoughByUserNickname(nickname));
    }

    @GetMapping("{nickname}/comments")
    public Response<List<CommentDto>> getCommentsByAuthorNickname(@PathVariable("nickname") String nickname) {
        return Response.success(commentService.getAllByAuthorNickname(nickname));
    }

    @PostMapping("signup")
    public Response<Void> signup(@Valid @RequestBody UserSignupRequest request) {
        userService.add(request);
        return Response.success();
    }

    @PostMapping("activate/{token}")
    public Response<Void> activate(@PathVariable String token) {
        userService.activate(token);
        return Response.success();
    }

    @PostMapping("password/forgot")
    public Response<Void> forgotPassword(@RequestBody String email) {
        userService.sendResetPasswordUrl(email);
        return Response.success();
    }

    @PostMapping("password/reset")
    public Response<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request, @RequestParam("email") String email) {
        userService.resetPassword(request, email);
        return Response.success();
    }

    @PostMapping("update")
    public Response<Void> updateInfo(Principal principal, @RequestBody String info) {
        userService.updateInfo(principal, info);
        return Response.success();
    }

}
