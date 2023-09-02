package com.example.educationapp.dto.request;

import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ДТО, которое передаёт админ при обновлении данных пользователя.")
public class UpdateUserDto {
    @NotBlank
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @Schema(description = "Список ролей пользователя", example = "[\"ROLE_ADMIN\"]")
    private Set<ERole> roleSet;

    @Email
    @Size(max = 50)
    @NotBlank
    @Schema(description = "Адрес электронной почты", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "John")
    private String firstname;

    @Schema(description = "Отчество пользователя", example = "Middle")
    private String middlename;

    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastname;

    @NotBlank
    @Size(min = 6, max = 40)
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @NotBlank
    @Schema(description = "Статус пользователя", example = "ACTIVE")
    private UserStatus status;
}
