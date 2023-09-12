package com.example.educationapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Информация о пользователе для методов безопасности")
public class UserInfoDto {
    private Long id;
    private String username;
    private String firstname;
    private String middlename;
    private String lastname;
}