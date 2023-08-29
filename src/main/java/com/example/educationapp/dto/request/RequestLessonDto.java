package com.example.educationapp.dto.request;

import com.example.educationapp.entity.LessonStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLessonDto {
    @NotBlank
    private String lessonName;

    @NotBlank
    private String content;

    @NotNull
    private LessonStatus lessonStatus;
}
