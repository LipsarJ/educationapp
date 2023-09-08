package com.example.educationapp.dto.response.student;

import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.MediaHomeworkTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ДТО для отображения информации о ДЗ для студента")
public class ResponseHomeworkTaskStudentDto {
    @Schema(description = "Идентификатор задания", example = "1")
    private Long id;
    @Schema(description = "Заголовок задания", example = "Домашнее задание 1")
    private String title;
    @Schema(description = "Описание задания", example = "Выполните упражнения на странице 10")
    private String description;
    /*@Schema(description = "Список медиа, прикреплённых к ДЗ")
    private List<MediaHomeworkTask> mediaHomeworkTasks;*/
    @Schema(description = "Дата срока выполнения", example = "2023-08-31T18:00:00Z")
    private OffsetDateTime deadlineDate;
}
