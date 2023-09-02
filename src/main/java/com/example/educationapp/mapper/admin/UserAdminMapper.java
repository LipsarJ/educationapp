package com.example.educationapp.mapper.admin;

import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.BaseLocalDateTimeOffsetDateTimeMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface UserAdminMapper {
    User toEntity(UpdateUserDto updateUserDto);

    UserAdminResponseDto toDto(User user);
}
