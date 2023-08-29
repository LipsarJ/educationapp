package com.example.educationapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record SignupDto(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Size(max = 50) @Email String email,
        @NotBlank @Size(min = 6, max = 40) String password,
        @NotBlank String middlename,
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotNull LocalDateTime createDate,
        @NotNull LocalDateTime updateDate,
        @NotNull String userStatus
) {
}