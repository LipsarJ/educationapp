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
    @Schema(description = "Идентификатор курса")
    private Long id;

    @Schema(description = "Название курса")
    private String courseName;

    @Schema(description = "Статус курса")
    private CourseStatus courseStatus;

    @Schema(description = "Дата обновления")
    private OffsetDateTime updateDate;

    @Schema(description = "Дата создания")
    private OffsetDateTime createDate;
}
