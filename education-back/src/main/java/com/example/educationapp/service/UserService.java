package com.example.educationapp.service;


import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.ERole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {
    Page<UserInfoDto> getUsersWithPaginationAndFilter(String filterText, Pageable pageable);

    Page<UserInfoDto> getAllUsersWithRole(ERole roleName, Pageable pageable);
}
