package com.example.educationapp.dto.request.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО для обновления пароля админом у пользователя.")
public class UpdatePasswordDto {
    @Schema(description = "Пароль пользователя (от 6 до 40 символов)", example = "Password123")
    @NotBlank
    @Size(min = 6, max = 40)
    String password;
}
