package com.example.educationapp.mapper;

import com.example.educationapp.dto.UserDto;
import com.example.educationapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
