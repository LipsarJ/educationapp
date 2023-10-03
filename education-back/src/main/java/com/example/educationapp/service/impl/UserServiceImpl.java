package com.example.educationapp.service.impl;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.User;
import com.example.educationapp.repo.RoleRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.repo.specification.UserSpecifications;
import com.example.educationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;


    @Override
    public Page<UserInfoDto> getUsersWithPaginationAndFilter(String filterText, Pageable pageable) {
        Specification<User> spec = UserSpecifications.searchByFilterText(filterText);
        Page<User> usersPage = userRepo.findAll(spec, pageable);

        return usersPage.map(user -> new UserInfoDto(user.getId(), user.getUsername(), user.getFirstname(), user.getMiddlename(), user.getLastname()));
    }

    @Override
    public Page<UserInfoDto> getAllUsersWithRole(ERole roleName, Pageable pageable) {
        Role role = roleRepo.findByRoleName(roleName);
        Page<User> usersPage = userRepo.findAllByRole(role, pageable);
        return usersPage.map(user -> new UserInfoDto(user.getId(), user.getUsername(), user.getFirstname(), user.getMiddlename(), user.getLastname()));
    }
}
