package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "ДТО регистрации, которое получаем при регистрации пользователя")
public record SignupDto(
        @Schema(description = "Имя пользователя для регистрации")
        @NotBlank
        @Size(min = 3, max = 20)
        String username,
        @Schema(description = "E-mail пользователя для регистрации")
        @NotBlank
        @Size(max = 50)
        @Email
        String email,
        @Schema(description = "Пароль пользователя для регистрации")
        @NotBlank
        @Size(min = 6, max = 40)
        String password,
        @Schema(description = "Отчество пользователя для регистрации")
        @NotBlank
        String middlename,
        @Schema(description = "Имя пользователя для регистрации")
        @NotBlank
        String firstname,
        @Schema(description = "Фамилия пользователя для регистрации")
        @NotBlank
        String lastname,
        @Schema(description = "Дата создания пользователя в базе данных")
        @NotNull
        LocalDateTime createDate,
        @Schema(description = "Дата обновления данных пользователя в базе данных")
        @NotNull
        LocalDateTime updateDate,
        @Schema(description = "Статус пользователя для регистрации")
        @NotNull
        String userStatus
) {
}