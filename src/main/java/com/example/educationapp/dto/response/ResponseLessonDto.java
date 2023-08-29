package com.example.educationapp.dto.response;

import com.example.educationapp.entity.LessonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonDto {
    private Long id;
    private String lessonName;
    private String content;
    private LessonStatus lessonStatus;
    private OffsetDateTime updateDate;
    private OffsetDateTime createDate;
}
