package com.example.educationapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.util.Set;

public record SignupDto(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Size(max = 50) @Email String email,
        Set<String> roles,
        @NotBlank @Size(min = 6, max = 40) String password,
        @NotBlank String middlename,
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotNull Timestamp createDate,
        @NotNull Timestamp updateDate,
        @NotNull String userStatus
) {}