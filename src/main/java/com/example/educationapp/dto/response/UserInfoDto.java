package com.example.educationapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Информация о пользователе для методов безопасности")
public record UserInfoDto(
        @Schema(description = "Идентификатор пользователя", example = "1")
        Long id,
        @Schema(description = "Имя пользователя", example = "Иванов")
        String username,
        @Schema(description = "Адрес электронной почты пользователя", example = "ivanov@example.com")
        String email,
        @Schema(description = "Список ролей пользователя", example = "[\"USER\"]")
        List<String> roles
) {
}