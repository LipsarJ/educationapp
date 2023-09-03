package com.example.educationapp.service;


import com.example.educationapp.dto.response.UserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserInfoDto> getUsersWithPaginationAndFilter(String filterText, Pageable pageable);
}
