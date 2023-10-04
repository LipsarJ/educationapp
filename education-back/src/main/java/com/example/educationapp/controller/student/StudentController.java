package com.example.educationapp.controller.student;

import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.service.student.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student/course")
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Получить все курсы для студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseCourseDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ResponseCourseDto> getAllCoursesForStudent() {
        return studentService.getAllCoursesForStudent();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о курсе для студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseCourseDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseCourseDto getCourseInfoForStudent(@PathVariable Long id) {
        return studentService.getCourseInfoForStudent(id);
    }

    @GetMapping("/{id}/lessons")
    @Operation(summary = "Получить информацию об уроках для студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseLessonDto.class))),
            @ApiResponse(responseCode = "404", description = "Урок не найден", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ResponseLessonDto> getLessonInfoForStudent(@PathVariable Long id) {
        return studentService.getAllLessons(id);
    }

    @GetMapping("/{id}/lessons/{lessonId}")
    @Operation(summary = "Получить информацию о уроке для студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseLessonDto.class))),
            @ApiResponse(responseCode = "404", description = "Урок не найден", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseLessonDto getLessonInfoForStudent(@PathVariable Long id, @PathVariable Long lessonId) {
        return studentService.getLessonInfoForStudent(id, lessonId);
    }

    @GetMapping("/{id}/lessons/{lessonId}/homeworks")
    @Operation(summary = "Получить информацию о домашних заданиях для студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkTaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Домашнее задание не найдено", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<ResponseHomeworkTaskDto> getAllTasksInfoForStudent(@PathVariable Long id, @PathVariable Long lessonId) {
        return studentService.getAllTasks(id, lessonId);
    }

    @GetMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}")
    @Operation(summary = "Получить информацию о домашнем задании для студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkTaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Домашнее задание не найдено", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseHomeworkTaskDto getHomeworkTaskInfoForStudent(@PathVariable Long id, @PathVariable Long lessonId,
                                                                 @PathVariable Long homeworkTaskId) {
        return studentService.getHomeworkTaskInfoForStudent(id, lessonId, homeworkTaskId);
    }

    @GetMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/my-homework")
    @Operation(summary = "Получить информацию о выполненной домашней работе студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkDoneStudentDto.class))),
            @ApiResponse(responseCode = "404", description = "Домашняя работа не найдена", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseHomeworkDoneStudentDto getStudentHomeworkDone(@PathVariable Long id, @PathVariable Long lessonId,
                                                                 @PathVariable Long homeworkTaskId) {
        return studentService.getStudentHomeworkDone(id, lessonId, homeworkTaskId);
    }

    @PostMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/my-homework")
    @Operation(summary = "Создать выполненную домашнюю работу студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешно создано", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkDoneStudentDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Домашнее задание не найдено", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseHomeworkDoneStudentDto createStudentHomeworkDone(@PathVariable Long id, @PathVariable Long lessonId,
                                                                    @PathVariable Long homeworkTaskId, @RequestBody RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto) {
        return studentService.createHomeworkDone(id, lessonId, homeworkTaskId, requestHomeworkDoneStudentDto);
    }

    @PutMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/my-homework")
    @Operation(summary = "Редактировать выполненную домашнюю работу студента")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отредактировано", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkDoneStudentDto.class))),
            @ApiResponse(responseCode = "400", description = "Неверный запрос", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Домашнее задание не найдено", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseHomeworkDoneStudentDto editStudentHomeworkDone(@PathVariable Long id, @PathVariable Long lessonId,
                                                                  @PathVariable Long homeworkTaskId, @RequestBody RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto) {
        return studentService.editStudentHomeworkDone(id, lessonId, homeworkTaskId, requestHomeworkDoneStudentDto);
    }
}
