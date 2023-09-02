package com.example.educationapp.service;

import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminApiService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseUserDto updateUser(UpdateUserDto updateUserDto, Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User is not found."));

        if (updateUserDto.getUsername() != null) {
            user.setUsername(updateUserDto.getUsername());
        }
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getFirstname() != null) {
            user.setFirstname(updateUserDto.getFirstname());
        }
        if (updateUserDto.getMiddlename() != null) {
            user.setMiddlename(updateUserDto.getMiddlename());
        }
        if (updateUserDto.getLastname() != null) {
            user.setLastname(updateUserDto.getLastname());
        }
        if (updateUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        if (updateUserDto.getStatus() != null) {
            user.setStatus(updateUserDto.getStatus());
        }
        if (!updateUserDto.getRoleSet().isEmpty()) {
            for (Role role : user.getRoleSet()) {
                role.getUsers().remove(user);
                roleRepo.save(role);
            }
            user.getRoleSet().clear();
            for (ERole eRole : updateUserDto.getRoleSet()) {
                Role role = roleRepo.findByRoleName(eRole);
                user.getRoleSet().add(role);
                role.getUsers().add(user);
                roleRepo.save(role);
            }
        }
        userRepo.save(user);
        return userMapper.toDto(user);
    }
}
