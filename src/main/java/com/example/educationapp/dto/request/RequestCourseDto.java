package com.example.educationapp.dto.request;

import com.example.educationapp.entity.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourseDto {
    @NotBlank
    private String courseName;

    @NotNull
    private CourseStatus courseStatus;
}
