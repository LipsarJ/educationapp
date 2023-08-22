package com.example.educationapp.dto.request;

import com.example.educationapp.entity.LessonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLessonDto {
    private String lessonName;
    private String content;
    private LessonStatus lessonStatus;
}
