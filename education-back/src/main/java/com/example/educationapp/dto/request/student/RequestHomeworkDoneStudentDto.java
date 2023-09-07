package com.example.educationapp.dto.request.student;

import com.example.educationapp.entity.MediaHomeworkDone;
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
@Schema(description = "ДТО для загрузки ДЗ от студента")
public class RequestHomeworkDoneStudentDto {
    @Schema(description = "Описание решения, приложенное студентом", example = "Это решение по параграфу 1")
    private String studentDescription;
    /*@Schema(description = "Медиа, которые хочет прикрепить студент", example = "")
    List<MediaHomeworkDone> mediaHomeworkDones;*/
}
