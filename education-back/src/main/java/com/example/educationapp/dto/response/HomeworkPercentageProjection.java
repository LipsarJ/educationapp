package com.example.educationapp.dto.response;

public interface HomeworkPercentageProjection {
    Long getLessonId();

    Long getStudentId();

    Double getPercentage();
}
