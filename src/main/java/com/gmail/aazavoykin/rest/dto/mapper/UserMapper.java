package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.Comment;
import com.gmail.aazavoykin.db.model.Story;
import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.rest.dto.UserDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "info", target = "info")
    UserDto userToUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);

    @IterableMapping()
    default Map<Long, String> stories(List<Story> stories) {
        return stories.stream().collect(Collectors.toMap(Story::getId, Story::getTitle));
    }

    @IterableMapping
    default Map<Long, String> comments(List<Comment> comments) {
        return comments.stream().collect(Collectors.toMap(Comment::getId, Comment::getBody));
    }

}
