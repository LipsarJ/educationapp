package com.example.educationapp.service.admin;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.UpdateUserDto;
import com.example.educationapp.dto.request.admin.UpdatePasswordDto;
import com.example.educationapp.dto.response.ResponseRoleDto;
import com.example.educationapp.dto.response.admin.UserAdminResponseDto;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.extend.UserNotFoundException;
import com.example.educationapp.mapper.admin.UserAdminMapper;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.repo.specification.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<UserAdminResponseDto> getUsersWithPaginationAndFilter(String filterText, Pageable pageable) {
        Specification<User> spec = UserSpecifications.searchByFilterText(filterText);
        Page<User> usersPage = userRepo.findAll(spec, pageable);

        return usersPage.map(user -> userAdminMapper.toDto(user));
    }

    @Transactional
    public UserAdminResponseDto updateUser(UpdateUserDto updateUserDto, Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User is not found."));

        if (userRepo.existsByUsernameAndIdNot(updateUserDto.getUsername(), id)) {
            throw new BadDataException(String.format("User with username: %s is already exists.", updateUserDto.getUsername()), Errors.USERNAME_TAKEN);
        }
        user.setUsername(updateUserDto.getUsername());
        if (userRepo.existsByEmailAndIdNot(updateUserDto.getEmail(), id)) {
            throw new BadDataException(String.format("User with email: %s is already exists.", updateUserDto.getEmail()), Errors.EMAIL_TAKEN);
        }
        user.setEmail(updateUserDto.getEmail());
        user.setFirstname(updateUserDto.getFirstname());
        user.setMiddlename(updateUserDto.getMiddlename());
        user.setLastname(updateUserDto.getLastname());
        user.setStatus(updateUserDto.getUserStatus());
        user.getRoleSet().clear();
        try {
            for (ResponseRoleDto responseRoleDto : updateUserDto.getRoles()) {
                Role role = roleRepo.findByRoleName(responseRoleDto.getRoleName());
                user.getRoleSet().add(role);
            }
        } catch (BadDataException ex) {
            throw new BadDataException("Roles are bad", Errors.BAD_CREDITIANS);
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
