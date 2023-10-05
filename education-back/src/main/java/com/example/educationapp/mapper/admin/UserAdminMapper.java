package com.example.educationapp.mapper.admin;

import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.BaseLocalDateTimeOffsetDateTimeMapper;
import com.example.educationapp.mapper.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BaseLocalDateTimeOffsetDateTimeMapper.class, RoleMapper.class})
public interface UserAdminMapper {
    User toEntity(UpdateUserDto updateUserDto);

    @Mapping(target = "userStatus", source = "user.status")
    @Mapping(target = "roles", source = "user.roleSet")
    UserAdminResponseDto toDto(User user);
}
