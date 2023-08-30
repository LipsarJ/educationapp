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
    @Schema(description = "Идентификатор задания")
    private Long id;

    @Schema(description = "Заголовок задания")
    private String title;

    @Schema(description = "Описание задания")
    private String description;

    @Schema(description = "Дата срока выполнения")
    private OffsetDateTime deadlineDate;

    @Schema(description = "Дата обновления")
    private OffsetDateTime updateDate;

    @Schema(description = "Дата создания")
    private OffsetDateTime createDate;
}
