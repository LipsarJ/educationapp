package com.example.educationapp.mapper;


import com.example.educationapp.dto.RoleDto;
import com.example.educationapp.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDto roleDto);
}
