package com.example.educationapp.dto.response;

import com.example.educationapp.entity.ERole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ДТО для отображения информации о роли")
public class ResponseRoleDto {
    private Long id;
    private ERole roleName;
}
