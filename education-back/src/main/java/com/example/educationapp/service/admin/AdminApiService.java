package com.example.educationapp.service.admin;

import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.request.admin.UpdatePasswordDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.mapper.admin.UserAdminMapper;
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
    private final UserAdminMapper userAdminMapper;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAdminResponseDto updateUser(UpdateUserDto updateUserDto, Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User is not found."));

        if (userRepo.existsByUsernameAndIdNot(updateUserDto.getUsername(), id)) {
            throw new BadDataException(String.format("User with username: %s is already exists.", updateUserDto.getUsername()));
        }
        user.setUsername(updateUserDto.getUsername());
        if (userRepo.existsByEmailAndIdNot(updateUserDto.getEmail(), id)) {
            throw new BadDataException(String.format("User with email: %s is already exists.", updateUserDto.getEmail()));
        }
        user.setEmail(updateUserDto.getEmail());
        user.setFirstname(updateUserDto.getFirstname());
        user.setMiddlename(updateUserDto.getMiddlename());
        user.setLastname(updateUserDto.getLastname());
        user.setStatus(updateUserDto.getUserStatus());
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
        userRepo.save(user);
        return userAdminMapper.toDto(user);
    }

    @Transactional
    public UserAdminResponseDto updateUserPassword(UpdatePasswordDto updatePasswordDto, Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User is not found."));
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        userRepo.save(user);
        return userAdminMapper.toDto(user);
    }
}
