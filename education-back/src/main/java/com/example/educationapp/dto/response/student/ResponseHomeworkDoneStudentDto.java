package com.example.educationapp.dto.response.student;

import com.example.educationapp.dto.response.UserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "ДТО для отображения информации о сделанном ДЗ для студента.")
public class ResponseHomeworkDoneStudentDto {
    @Schema(description = "Идентификатор задания")
    private Long id;
    @Schema(description = "Дата загрузки ДЗ")
    private LocalDateTime submissionDate;
    @Schema(description = "Оценка за задание")
    private Integer grade;
    @Schema(description = "Описание от студента")
    private String studentDescription;
    @Schema(description = "Обратная связь от учителя")
    private String teacherFeedback;
    @Schema(description = "Данные учителя, проверившего задание")
    private UserInfoDto teacherInfoDto;
    @Schema(description = "Данные студента, загрузившего задание")
    private UserInfoDto studentInfoDto;
    //private List<MediaHomeworkDone> mediaHomeworkDones;
}
