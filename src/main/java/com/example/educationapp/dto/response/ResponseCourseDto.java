package com.example.educationapp.dto.response;

import com.example.educationapp.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCourseDto {
    private String courseName;
    private CourseStatus courseStatus;
}
