package com.example.educationapp.mapper;

import com.example.educationapp.dto.response.ResponseRoleDto;
import com.example.educationapp.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface RoleMapper {
    ResponseRoleDto toDto(Role role);
    Role toEntity(ResponseRoleDto roleDto);
}
