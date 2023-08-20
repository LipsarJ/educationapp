package com.example.educationapp.dto;

import com.example.educationapp.entity.LessonStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {
    private Long id;
    private String lessonName;
    private String content;
    private LessonStatus status;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
