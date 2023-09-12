package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.controlleradvice.ErrorResponse;
import com.example.educationapp.dto.request.teacher.RequestTeacherCheckHomeworkDto;
import com.example.educationapp.dto.response.HomeworkDoneInfoPage;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.service.management.teacher.TeacherHomeworkCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher/course")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('TEACHER')")
public class TeacherHomeworkCheckController {
    private final TeacherHomeworkCheckService teacherHomeworkCheckService;

    @GetMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}")
    @Operation(summary = "Получить информацию о сделанных ДЗ для задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение информации о сделанных ДЗ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HomeworkDoneInfoPage.class))),
            @ApiResponse(responseCode = "404", description = "Урок, задача или ДЗ не найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Введены неверные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public HomeworkDoneInfoPage getAllHomeworksDoneForTask(@PathVariable Long id, @PathVariable Long lessonId,
                                                           @PathVariable Long homeworkTaskId,
                                                           Pageable pageable,
                                                           @RequestParam(required = false) Boolean checked
    ) {
        Page<ResponseHomeworkDoneStudentDto> homeworksDonePage =
                teacherHomeworkCheckService.getAllHomeworksDoneForTask(id, lessonId, homeworkTaskId, pageable, checked);
        return new HomeworkDoneInfoPage(
                homeworksDonePage.getContent(),
                homeworksDonePage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PutMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/{homeworkDoneId}")
    @Operation(summary = "Установить оценку для выполненного ДЗ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное установление оценки для выполненного ДЗ",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseHomeworkDoneStudentDto.class))),
            @ApiResponse(responseCode = "404", description = "Урок, задача, выполненное ДЗ или студент не найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Введены неверные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseHomeworkDoneStudentDto setGradeToHomework(@PathVariable Long id, @PathVariable Long lessonId,
                                                             @PathVariable Long homeworkTaskId, @PathVariable Long homeworkDoneId,
                                                             @RequestBody RequestTeacherCheckHomeworkDto requestTeacherCheckHomeworkDto) {
        return teacherHomeworkCheckService.setGradeToHomeworkDone(id, lessonId, homeworkTaskId, homeworkDoneId, requestTeacherCheckHomeworkDto);
    }
}
