package com.example.educationapp.mapper;

import com.example.educationapp.dto.UserDto;
import com.example.educationapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true) // Игнорировать id при маппинге сущности на DTO
    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
