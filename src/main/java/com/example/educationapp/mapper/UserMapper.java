package com.example.educationapp.mapper;

import com.example.educationapp.dto.request.RequestUserDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface UserMapper {
    User toEntity(RequestUserDto requestUserDto);

    @Mapping(target = "roles", source = "roleSet")
    ResponseUserDto toDto(User user);
}
