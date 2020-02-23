package com.gmail.aazavoykin.rest.controller;

import com.gmail.aazavoykin.rest.dto.StoryDto;
import com.gmail.aazavoykin.rest.dto.mapper.StoryMapper;
import com.gmail.aazavoykin.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    @Autowired
    private final StoryService storyService;

    @GetMapping("/stories")
    public List<StoryDto> all() {
        return storyService.getAll();
    }

    @GetMapping("/last10")
    public List<StoryDto> last10() {
        return storyService.getLast10();
    }

    @GetMapping("/{id}")
    public StoryDto getById(@PathVariable("id") Long id) {
        return storyService.getById(id);
    }

    @GetMapping("/tag/{tag}")
    public List<StoryDto> getByTag(@PathVariable("tag") String tagname) {
        return storyService.getAlByTag(tagname);
    }

    @PostMapping("/add")
    public StoryDto add(StoryDto dto) {
        return StoryMapper.INSTANCE.storyToStoryDto(storyService.save(dto));
    }

    @PostMapping("/update")
    public StoryDto update(StoryDto dto) {
        return StoryMapper.INSTANCE.storyToStoryDto(storyService.update(dto));
    }

    @PostMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        storyService.delete(id);
    }

}
