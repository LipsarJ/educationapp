package com.example.educationapp.dto;

import com.example.educationapp.entity.LessonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private Long id;
    private String lessonName;
    private String content;
    private LessonStatus status;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;
}
