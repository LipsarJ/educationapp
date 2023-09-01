package com.example.educationapp.service.impl;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.repo.specification.UserSpecifications;
import com.example.educationapp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Page<UserInfoDto> getUsersWithPaginationAndFilter(String filterText, Pageable pageable) {
        try {
            Specification<User> spec = UserSpecifications.searchByFilterText(filterText);
            Page<User> usersPage = userRepo.findAll(spec, pageable);

            if (usersPage.isEmpty()) {
                throw new UserNotFoundException("No users found with the given filter on this page.");
            }
            return usersPage.map(user -> new UserInfoDto(user.getId(), user.getUsername(), user.getFirstname(), user.getMiddlename(), user.getLastname()));
        } catch (RuntimeException ex) {
            throw new BadDataException("Bad sort field: " + pageable.getSort().toString());
        }
    }
}
