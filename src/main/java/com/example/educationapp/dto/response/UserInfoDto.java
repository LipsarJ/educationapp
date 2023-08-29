package com.example.educationapp.dto.response;

import java.util.List;

public record UserInfoDto(
        Long id,
        String username,
        String email,
        List<String> roles
) {
}