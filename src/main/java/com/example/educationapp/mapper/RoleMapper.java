package com.example.educationapp.mapper;


import com.example.educationapp.dto.RoleDto;
import com.example.educationapp.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDto roleDto);
}
