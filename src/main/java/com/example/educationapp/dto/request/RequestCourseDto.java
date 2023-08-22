package com.example.educationapp.dto.request;

import com.example.educationapp.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourseDto {
    private String courseName;
    private CourseStatus courseStatus;
}
