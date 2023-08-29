package com.example.educationapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank String username,
        @NotBlank String password
) {
}