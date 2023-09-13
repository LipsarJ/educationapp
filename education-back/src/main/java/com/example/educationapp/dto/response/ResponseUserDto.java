package com.example.educationapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@Schema(description = "ДТО с информацией о пользователе")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseUserDto {
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иванов")
    private String username;

    @Schema(description = "Набор ролей пользователя", example = "[\"USER\", \"ADMIN\"]")
    private Set<ResponseRoleDto> roles;

    @Schema(description = "Дата обновления", example = "2023-08-24T14:30:00Z")
    private OffsetDateTime updateDate;
}

