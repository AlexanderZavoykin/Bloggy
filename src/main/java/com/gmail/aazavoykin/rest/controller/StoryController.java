package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.response.Response;
import com.gmail.aazavoykin.service.CommentService;
import com.gmail.aazavoykin.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("story")
public class StoryController {

    private final StoryService storyService;
    private final CommentService commentService;

    @GetMapping("stories")
    public Response<List<StoryDto>> all() {
        return Response.success(storyService.getAll());
    }

    @GetMapping("last10")
    public Response<List<StoryDto>> last10() {
        return Response.success(storyService.getLast10());
    }

    @GetMapping("{id}")
    public Response<StoryDto> getById(@PathVariable("id") Long id) {
        return Response.success(storyService.getById(id));
    }

    @GetMapping("tag/{tag}")
    public Response<List<StoryDto>> getByTag(@PathVariable("tag") String tagname) {
        return Response.success(storyService.getAlByTag(tagname));
    }

    @PostMapping("{id}/comment")
    public Response<Void> addComment(@PathVariable("id") Long storyId, @Valid @RequestBody String commentBody) {
        commentService.addComment(storyId, commentBody);
        return Response.success();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("add")
    public Response<StoryDto> add(StoryDto dto) {
        return Response.success(storyService.save(dto));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("update")
    public Response<StoryDto> update(StoryDto dto) {
        return Response.success(storyService.update(dto));
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("delete/{id}")
    public Response<Void> delete(@PathVariable("id") Long id) {
        storyService.delete(id);
        return Response.success();
    }
}
