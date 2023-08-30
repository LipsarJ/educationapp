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
    @Schema(description = "Идентификатор занятия")
    private Long id;

    @Schema(description = "Название занятия")
    private String lessonName;

    @Schema(description = "Содержание занятия")
    private String content;

    @Schema(description = "Статус занятия")
    private LessonStatus lessonStatus;

    @Schema(description = "Дата обновления")
    private OffsetDateTime updateDate;

    @Schema(description = "Дата создания")
    private OffsetDateTime createDate;
}
