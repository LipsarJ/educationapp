package com.example.educationapp.mapper;

import com.example.educationapp.dto.UserDto;
import com.example.educationapp.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);
}