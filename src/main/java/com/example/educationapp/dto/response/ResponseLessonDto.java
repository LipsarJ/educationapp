package com.example.educationapp.dto.response;

import com.example.educationapp.entity.LessonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonDto {
    private String lessonName;
    private String content;
    private LessonStatus lessonStatus;
}
