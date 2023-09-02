package com.example.educationapp.dto.request;

import com.example.educationapp.entity.ERole;
import com.example.educationapp.entity.UserStatus;
import jakarta.validation.constraints.Email;
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
public class UpdateUserDto {
    private String username;
    private Set<ERole> roleSet;
    @Email
    @Size(max = 50)
    private String email;
    private String firstname;
    private String middlename;
    private String lastname;
    @Size(min = 6, max = 40)
    private String password;
    private UserStatus status;
}
