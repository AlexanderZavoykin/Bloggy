package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.CommentDto;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.UserDto;
import com.gmail.aazavoykin.rest.request.ChangePasswordRequest;
import com.gmail.aazavoykin.rest.request.ForgotPasswordRequest;
import com.gmail.aazavoykin.rest.request.UpdateUserInfoRequest;
import com.gmail.aazavoykin.rest.request.UserSignupRequest;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.CommentService;
import com.gmail.aazavoykin.service.StoryService;
import com.gmail.aazavoykin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final StoryService storyService;
    private final CommentService commentService;

    @GetMapping("users")
    public Response<List<UserDto>> all() {
        return Response.success(userService.getAll());
    }

    @GetMapping("{nickname}")
    public Response<UserDto> userInfo(@PathVariable("nickname") String nickname) {
        return Response.success(userService.getByNickname(nickname));
    }

    @GetMapping("{nickname}/stories")
    public Response<List<StoryDto>> getStoriesByAuthorNickname(@PathVariable("nickname") String nickname) {
        return Response.success(storyService.getAllByUserNickname(nickname));
    }

    @GetMapping("{nickname}/comments")
    public Response<List<CommentDto>> getCommentsByAuthorNickname(@PathVariable("nickname") String nickname) {
        return Response.success(commentService.getAllByAuthorNickname(nickname));
    }

    @PostMapping("auth/signup")
    public Response<Void> signup(@Valid @RequestBody UserSignupRequest request) {
        userService.add(request);
        return Response.success();
    }

    @PostMapping("auth/activate/{token}")
    public Response<Void> activate(@PathVariable String token) {
        userService.activate(token);
        return Response.success();
    }

    @PostMapping("password/forgot")
    public Response<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.sendResetPasswordUrl(request.getEmail());
        return Response.success();
    }

    @PostMapping("password/change/{token}")
    public Response<Void> resetPassword(@Valid @RequestBody ChangePasswordRequest request, @PathVariable("token") String token) {
        userService.changePassword(request, token);
        return Response.success();
    }

    @PostMapping("update")
    public Response<Void> updateInfo(@RequestBody UpdateUserInfoRequest request) {
        userService.updateInfo(request);
        return Response.success();
    }
}
