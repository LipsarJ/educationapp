package com.example.educationapp.controller.teacher;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.teacher.TeacherHomeworkTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher/homework-tasks")
@PreAuthorize("hasAuthority('TEACHER')")
@RequiredArgsConstructor
public class TeacherHomeworkTaskController {
    private final TeacherHomeworkTaskService teacherHomeworkTaskService;

    @GetMapping("/{courseId}/{lessonId}")
    @Operation(summary = "Получить все задания для урока")
    @ApiResponse(responseCode = "200", description = "Список всех заданий для урока",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkTaskDto.class)))
    public ResponseEntity<List<ResponseHomeworkTaskDto>> getAllTasks(@PathVariable Long courseId, @PathVariable Long lessonId) {
        List<ResponseHomeworkTaskDto> tasks = teacherHomeworkTaskService.getAllTasks(courseId, lessonId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{courseId}/{lessonId}/{id}")
    @Operation(summary = "Получить информацию о задании по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию о задании",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseHomeworkTaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Если задание не найдено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<ResponseHomeworkTaskDto> getTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                           @PathVariable Long id) {
        ResponseHomeworkTaskDto responseHomeworkTaskDto = teacherHomeworkTaskService.getTask(courseId, lessonId, id);
        return ResponseEntity.ok(responseHomeworkTaskDto);
    }
}
