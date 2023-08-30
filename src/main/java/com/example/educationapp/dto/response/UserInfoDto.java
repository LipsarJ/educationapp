package com.example.educationapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Информация о пользователе для методов безопасности")
public record UserInfoDto(
        @Schema(description = "Идентификатор пользователя")
        Long id,
        @Schema(description = "Имя пользователя")
        String username,
        @Schema(description = "Адрес электронной почты пользователя")
        String email,
        @Schema(description = "Список ролей пользователя")
        List<String> roles
) {
}