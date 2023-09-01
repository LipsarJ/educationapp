package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestUserDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.UserHasRoleException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminApiService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final RoleRepo roleRepo;

    @Transactional
    public ResponseUserDto updateUser(RequestUserDto requestUserDto) {
        User user = userRepo.findByUsername(requestUserDto.getUsername()).orElseThrow(() -> new UserNotFoundException("User is not found."));
        for (String roleName : requestUserDto.getRoles()) {
            ERole eRole = ERole.valueOf(roleName);
            Role role = roleRepo.findByRoleName(eRole);
            if (!user.getRoleSet().contains(role)) {
                user.getRoleSet().add(role);
                role.getUsers().add(user);
                roleRepo.save(role);
            } else {
                throw new UserHasRoleException("User is already has this role: " + roleName);
            }
        }
        user = userRepo.save(user);
        return userMapper.toDto(user);
    }
}
