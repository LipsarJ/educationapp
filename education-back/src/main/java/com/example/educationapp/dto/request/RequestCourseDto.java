package com.example.educationapp.dto.request;

import com.example.educationapp.entity.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "ДТО для курса, который получаем при запросе от пользователя")
public class RequestCourseDto {
    @NotBlank
    @Size(min = 3, max = 20)
    @Schema(description = "Имя курса, которое вводит пользователь", example = "Введение в программирование")
    private String courseName;

    @NotNull
    @Schema(description = "Статус курса, который вводит пользователь", example = "ONGOING")
    private CourseStatus courseStatus;
}
