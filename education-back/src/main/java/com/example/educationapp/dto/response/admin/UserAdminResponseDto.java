package com.example.educationapp.dto.response.admin;

import com.example.educationapp.entity.Role;
import com.example.educationapp.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО, которое выводится админу.")
public class UserAdminResponseDto {
    private Long id;
    private String email;
    private String username;
    private String firstname;
    private String middlename;
    private String lastname;
    private Set<Role> roleSet;
    private UserStatus userStatus;
    private OffsetDateTime updateDate;
    private OffsetDateTime createDate;
}
