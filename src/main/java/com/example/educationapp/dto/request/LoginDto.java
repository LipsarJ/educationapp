package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "ДТО для логина пользователя.")
public record LoginDto(
       @Schema(description = "Имя пользователя для логина")
       @NotBlank
       String username,
       @Schema(description = "Пароль пользователя для логина")
       @NotBlank
       String password
) {
}