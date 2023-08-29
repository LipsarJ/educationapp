package com.example.educationapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDto {
    @NotBlank
    private String username;
    @NotBlank
    private Set<String> roles;
}
