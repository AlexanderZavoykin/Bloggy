package com.gmail.aazavoykin.rest.dto.mapper;

import com.gmail.aazavoykin.db.model.User;
import com.gmail.aazavoykin.rest.dto.ExtendedUserDto;
import com.gmail.aazavoykin.rest.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExtendedUserMapper {

    ExtendedUserDto userToExtendedUserDto(User user);

    List<ExtendedUserDto> usersToExtendedUserDtos(List<User> users);
}
