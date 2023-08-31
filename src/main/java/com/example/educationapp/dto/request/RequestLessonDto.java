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
    @Schema(description = "Имя урока, которое получаем от пользователя", example = "Переменные и типы данных")
    private String lessonName;

    @NotBlank
    @Schema(description = "Содержание урока, которое получаем от пользователя", example = "В этом уроке мы узнаем о различных типах переменных.")
    private String content;

    @NotNull
    @Schema(description = "Статус урока, которое получаем от пользователя", example = "В_ПРОЦЕССЕ")
    private LessonStatus lessonStatus;
}
