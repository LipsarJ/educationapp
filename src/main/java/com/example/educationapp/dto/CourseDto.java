package com.example.educationapp.dto;

import com.example.educationapp.entity.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private Long id;
    private String courseName;
    private CourseStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}