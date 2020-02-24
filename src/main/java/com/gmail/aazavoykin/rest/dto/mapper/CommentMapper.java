package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.Comment;
import com.gmail.aazavoykin.rest.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "authorId")
    @Mapping(source = "user.nickname", target = "authorNickname")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd hh:mm:ss")
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> commentsToCommentDtos(List<Comment> comments);

}
