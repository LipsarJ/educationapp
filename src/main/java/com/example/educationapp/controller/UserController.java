package com.example.educationapp.controller;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.dto.response.UserInfoPage;
import com.example.educationapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public UserInfoPage getUsersWithPagination(
            @RequestParam(name = "filterText", required = false) String filterText,
            Pageable pageable) {
        Page<UserInfoDto> userPage = userService.getUsersWithPaginationAndFilter(filterText, pageable);

        return new UserInfoPage(
                userPage.getContent(),
                userPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }
}
