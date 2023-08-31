package com.example.educationapp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО домашнего задания, которое получаем при запросе от пользователя")
public class RequestHomeworkTaskDto {
    @NotBlank
    @Schema(description = "Заголовок ДЗ, который получаем от пользователя", example = "Домашнее задание 1")
    private String title;

    @NotBlank
    @Schema(description = "Описание ДЗ, которое получаем от пользователя", example = "Выполните упражнения на странице 10")
    private String description;

    @NotBlank
    @Schema(description = "Срок сдачи ДЗ, который получаем от пользователя", example = "2023-08-10T12:00:00Z")
    private OffsetDateTime deadlineDate;
}
