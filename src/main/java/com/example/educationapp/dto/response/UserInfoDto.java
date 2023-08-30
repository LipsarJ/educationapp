package com.example.educationapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
}
