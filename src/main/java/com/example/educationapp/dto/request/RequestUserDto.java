package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО пользователя, которое получаем при запросе от админа")
public class RequestUserDto {
    @NotBlank
    @Schema(description = "Имя пользователя, которое получаем при запросе")
    private String username;
    @NotBlank
    @Schema(description = "Сет ролей пользователя, которые получаем при запросе")
    private Set<String> roles;
}
