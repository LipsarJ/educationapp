package com.example.educationapp.dto;

import com.example.educationapp.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String lastname;
    private String middlename;
    private String firstname;
    private UserStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}