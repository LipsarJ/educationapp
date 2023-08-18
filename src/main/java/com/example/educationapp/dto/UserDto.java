package com.example.educationapp.dto;

import com.example.educationapp.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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