package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.Comment;
import com.gmail.aazavoykin.rest.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "authorId")
    @Mapping(source = "user.nickname", target = "authorNickname")
    @Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd hh:mm:ss")
    @Mapping(source = "body", target = "body")
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> commentsToCommentDtos(List<Comment> comments);

}
