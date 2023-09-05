package com.example.educationapp.dto.response;

import com.example.educationapp.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Schema(description = "ДТО с информацией о пользователе")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иванов")
    private String username;

    @Schema(description = "Набор ролей пользователя", example = "[\"USER\", \"ADMIN\"]")
    private Set<Role> roles;

    @Schema(description = "Дата обновления", example = "2023-08-24T14:30:00Z")
    private OffsetDateTime updateDate;
}

