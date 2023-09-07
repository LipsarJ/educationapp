package com.example.educationapp.dto.response.student;

import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.MediaLesson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ДТО для выдачи информации о уроке для студентов.")
public class ResponseLessonStudentDto {
    @Schema(description = "Идентификатор занятия", example = "1")
    private Long id;
    @Schema(description = "Название занятия", example = "Введение в алгебру")
    private String lessonName;
    @Schema(description = "Содержание занятия", example = "Основные понятия и операции в алгебре")
    private String content;
    /*@Schema(description = "Список медиа прикреплённых к уроку")
    private List<MediaLesson> mediaLessons;*/
    @Schema(description = "Список домашних заданий", example = "Задание 1, задание 2.")
    private List<HomeworkTask> homeworkTasks;
}
