package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.TagDto;
import com.gmail.aazavoykin.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("tag")
public class TagController {

    private final TagService tagService;

    @GetMapping("tags")
    public List<TagDto> getAll() {
        return tagService.getAll();
    }

}
