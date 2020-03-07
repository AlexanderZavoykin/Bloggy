package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.Tag;
import com.gmail.aazavoykin.rest.dto.TagDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagDto tagToTagDto(Tag tag);

    List<TagDto> tagsToTagDtos(List<Tag> tags);
}
