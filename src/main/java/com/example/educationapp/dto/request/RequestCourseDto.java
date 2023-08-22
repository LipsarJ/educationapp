package com.example.educationapp.dto.request;

import com.example.educationapp.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourseDto {
    private Long id;
    private String courseName;
    private CourseStatus courseStatus;
    private OffsetDateTime updateDate;
}
