package com.example.educationapp.dto.response;

import com.example.educationapp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {
    private Long id;
    private String username;
    private Set<Role> roles;
    private OffsetDateTime updateDate;
}
