package com.example.educationapp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Schema(description = "ДТО с информацией о домашнем задании")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHomeworkTaskDto {
    @Schema(description = "Идентификатор задания", example = "1")
    private Long id;

    @Schema(description = "Заголовок задания", example = "Домашнее задание 1")
    private String title;

    @Schema(description = "Описание задания", example = "Выполните упражнения на странице 10")
    private String description;

    @Schema(description = "Дата срока выполнения", example = "2023-08-31T18:00:00Z")
    private OffsetDateTime deadlineDate;

    @Schema(description = "Дата обновления", example = "2023-08-24T14:30:00Z")
    private OffsetDateTime updateDate;

    @Schema(description = "Дата создания", example = "2023-08-20T12:00:00Z")
    private OffsetDateTime createDate;
}
