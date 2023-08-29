package com.example.educationapp.controller;

import com.example.educationapp.dto.request.RequestUserDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.service.AdminApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin/user")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminApiController {

    private final AdminApiService adminApiService;

    @PutMapping("/roles")
    public ResponseUserDto updateUserRoles(@RequestBody RequestUserDto requestUserDto) {
        return adminApiService.updateUser(requestUserDto);
    }
}
