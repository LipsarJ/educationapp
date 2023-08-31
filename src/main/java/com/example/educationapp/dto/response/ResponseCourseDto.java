package com.example.educationapp.dto.response;

import com.example.educationapp.entity.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "ДТО с информацией о курсе")
public class ResponseCourseDto {
    @Schema(description = "Идентификатор курса", example = "1")
    private Long id;

    @Schema(description = "Название курса", example = "Математика")
    private String courseName;

    @Schema(description = "Статус курса", example = "ACTIVE")
    private CourseStatus courseStatus;

    @Schema(description = "Дата обновления", example = "2023-08-24T14:30:00Z")
    private OffsetDateTime updateDate;

    @Schema(description = "Дата создания", example = "2023-08-20T10:00:00Z")
    private OffsetDateTime createDate;
}
