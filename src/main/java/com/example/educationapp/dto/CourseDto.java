package com.example.educationapp.dto;

import com.example.educationapp.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;
    private String courseName;
    private CourseStatus status;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;
}