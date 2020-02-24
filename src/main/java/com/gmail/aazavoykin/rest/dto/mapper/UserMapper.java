package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.rest.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    List<UserDto> usersToUserDtos(List<User> users);

}
