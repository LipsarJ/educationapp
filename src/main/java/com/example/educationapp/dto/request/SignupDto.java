package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "ДТО регистрации, которое получаем при регистрации пользователя")
public record SignupDto(
        @NotBlank
        @Size(min = 3, max = 20)
        @Schema(description = "Имя пользователя для регистрации", example = "анна_смирнова")
        String username,
        @NotBlank
        @Size(max = 50)
        @Email
        @Schema(description = "E-mail пользователя для регистрации", example = "anna@example.com")
        String email,
        @NotBlank
        @Size(min = 6, max = 40)
        @Schema(description = "Пароль пользователя для регистрации", example = "секретныйпароль")
        String password,
        @NotBlank
        @Schema(description = "Отчество пользователя для регистрации", example = "Ивановна")
        String middlename,
        @NotBlank
        @Schema(description = "Имя пользователя для регистрации", example = "Анна")
        String firstname,
        @NotBlank
        @Schema(description = "Фамилия пользователя для регистрации", example = "Смирнова")
        String lastname,
        @NotNull
        @Schema(description = "Дата создания пользователя в базе данных", example = "2023-08-24T12:00:00Z")
        LocalDateTime createDate,
        @NotNull
        @Schema(description = "Дата обновления данных пользователя в базе данных", example = "2023-08-24T12:00:00Z")
        LocalDateTime updateDate,
        @NotNull
        @Schema(description = "Статус пользователя для регистрации", example = "АКТИВНЫЙ")
        String userStatus
) {
        // Constructors and methods
}