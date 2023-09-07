package com.example.educationapp.dto.response.student;

import com.example.educationapp.entity.Lesson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(description = "ДТО для выдачи информации о курсе для студентов.")
public class ResponseCourseStudentDto {
    @Schema(description = "Идентификатор курса", example = "1")
    private Long id;
    @Schema(description = "Название курса", example = "Математика")
    private String courseName;
    @Schema(description = "Список уроков курса", example = "Вводный урок")
    private List<Lesson> lessons;
}
