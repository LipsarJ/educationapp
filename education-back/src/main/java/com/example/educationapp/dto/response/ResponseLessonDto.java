package com.example.educationapp.dto.response;

import com.example.educationapp.entity.LessonStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Schema(description = "ДТО с информацией о занятии")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonDto {
    @Schema(description = "Идентификатор занятия", example = "1")
    private Long id;

    @Schema(description = "Название занятия", example = "Введение в алгебру")
    private String lessonName;

    @Schema(description = "Содержание занятия", example = "Основные понятия и операции в алгебре")
    private String content;

    @Schema(description = "Порядковый номер урока", example = "1")
    private Integer num;

    @Schema(description = "Статус занятия", example = "ACTIVE")
    private LessonStatus lessonStatus;

    @Schema(description = "Дата обновления", example = "2023-08-24T14:30:00Z")
    private OffsetDateTime updateDate;

    @Schema(description = "Дата создания", example = "2023-08-20T13:30:00Z")
    private OffsetDateTime createDate;
}
