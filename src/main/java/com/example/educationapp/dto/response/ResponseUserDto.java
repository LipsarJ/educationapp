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
    @Schema(description = "Идентификатор пользователя")
    private Long id;

    @Schema(description = "Имя пользователя")
    private String username;

    @Schema(description = "Набор ролей пользователя")
    private Set<Role> roles;

    @Schema(description = "Дата обновления")
    private OffsetDateTime updateDate;
}

