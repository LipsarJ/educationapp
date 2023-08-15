package com.example.educationapp.dto;

import com.example.educationapp.entity.UserStatus;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String lastname;
    private String middlename;
    private String firstname;
    private String password;
    private UserStatus status;
}
