package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.rest.dto.StoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TagMapper.class, CommentMapper.class})
public interface StoryMapper {

    @Mapping(source = "user.nickname", target = "authorNickname")
    @Mapping(source = "user.id", target = "authorId")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd hh:mm:ss")
    @Mapping(source = "updated", target = "updated", dateFormat = "yyyy-MM-dd hh:mm:ss")
    @Mapping(source = "tags", target = "tags")
    StoryDto storyToStoryDto(Story story);

    List<StoryDto> storiesToStoryDtos(List<Story> stories);

}
