package com.example.educationapp.dto.request;

import com.example.educationapp.entity.LessonStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО для урока, которое получаем при запросе от пользователя")
public class RequestLessonDto {
    @NotBlank
    @Schema(description = "Имя урока, которое получаем от пользователя")
    private String lessonName;

    @NotBlank
    @Schema(description = "Содержание урока, которое получаем от пользователя")
    private String content;

    @NotNull
    @Schema(description = "Статус урока, которое получаем от пользователя")
    private LessonStatus lessonStatus;
}
