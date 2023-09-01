package com.example.educationapp.dto.response;

import java.util.List;

public record UserLoginDto(
        Long id,
        String username,
        String email,
        List<String> roles
) {
}