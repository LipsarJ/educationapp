package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "ДТО для логина пользователя.")
public record LoginDto(
        @NotBlank
        @Schema(description = "Имя пользователя для логина", example = "Lipsar")
        String username,
        @NotBlank
        @Schema(description = "Пароль пользователя для логина", example = "admin111")
        String password
) {
}