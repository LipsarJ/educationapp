package com.example.educationapp.dto.response;

import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
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
@Schema(description = "Страница с информацией о решении ДЗ")
public class HomeworkDoneInfoPage {
    private List<ResponseHomeworkDoneStudentDto> homeworkDoneInfo;
    private long totalCount;
    private int page;
    private int countValuesPerPage;
}
