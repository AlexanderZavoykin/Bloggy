package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.model.Story;
import com.gmail.aazavoykin.model.Tag;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface StoryMapper {

    StoryMapper INSTANCE = Mappers.getMapper(StoryMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "user.nickname", target = "authorNickname")
    @Mapping(source = "user.id", target = "authorId")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd hh:mm:ss")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "body", target = "body")
    StoryDto storyToStoryDto(Story story);

    List<StoryDto> storiesToStoryDtos(List<Story> stories);

    @IterableMapping
    default List<String> tags(List<Tag> tags) {
        return tags.stream().map(Tag::getName).collect(Collectors.toList());
    }

}
