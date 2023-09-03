package com.example.educationapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Информация о пользователе для методов безопасности")
public class UserInfoDto {
        private Long id;
        private String username;
        private String firstName;
        private String middleName;
        private String lastName;
}