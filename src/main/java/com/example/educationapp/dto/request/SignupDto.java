package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "ДТО регистрации, которое получаем при регистрации пользователя")
public record SignupDto(
        @Schema(description = "Имя пользователя для регистрации (от 3 до 20 символов)", example = "Ivan_Iv")
        @NotBlank
        @Size(min = 3, max = 20)
        String username,

        @Schema(description = "Адрес электронной почты пользователя", example = "Ivam.iv@example.com")
        @NotBlank
        @Size(max = 50)
        @Email
        String email,

        @Schema(description = "Пароль пользователя (от 6 до 40 символов)", example = "Password123")
        @NotBlank
        @Size(min = 6, max = 40)
        String password,

        @Schema(description = "Отчество пользователя", example = "Иванович")
        @NotBlank
        String middlename,

        @Schema(description = "Имя пользователя", example = "Иван")
        @NotBlank
        String firstname,

        @Schema(description = "Фамилия пользователя", example = "Иванов")
        @NotBlank
        String lastname
) {
}