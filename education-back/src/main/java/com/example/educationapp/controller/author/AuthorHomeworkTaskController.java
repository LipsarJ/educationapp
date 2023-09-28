package com.example.educationapp.controller.author;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.service.AuthorHomeworkTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/author/homework-tasks")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorHomeworkTaskController {

    private final AuthorHomeworkTaskService authorHomeworkTaskService;

    @GetMapping("/{courseId}/{lessonId}")
    @Operation(summary = "Получить все задания для урока")
    @ApiResponse(responseCode = "200", description = "Список всех заданий для урока",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkTaskDto.class)))
    public ResponseEntity<List<ResponseHomeworkTaskDto>> getAllTasks(@PathVariable Long courseId, @PathVariable Long lessonId) {
        List<ResponseHomeworkTaskDto> tasks = authorHomeworkTaskService.getAllTasks(courseId, lessonId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{courseId}/{lessonId}")
    @Operation(summary = "Создать новое задание для урока")
    @ApiResponse(responseCode = "200", description = "Возвращает информацию о созданном задании",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseHomeworkTaskDto.class)))
    public ResponseEntity<ResponseHomeworkTaskDto> createTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                              @RequestBody RequestHomeworkTaskDto requestHomeworkTaskDto) {
        return ResponseEntity.ok(authorHomeworkTaskService.createTask(courseId, lessonId, requestHomeworkTaskDto));
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
        ResponseHomeworkTaskDto responseHomeworkTaskDto = authorHomeworkTaskService.getTask(courseId, lessonId, id);
        return ResponseEntity.ok(responseHomeworkTaskDto);
    }

    @PutMapping("/{courseId}/{lessonId}/{id}")
    @Operation(summary = "Обновить информацию о задании по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию об обновленном задании",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseHomeworkTaskDto.class))),
            @ApiResponse(responseCode = "404", description = "Если задание не найдено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<ResponseHomeworkTaskDto> updateTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                              @PathVariable Long id, @RequestBody RequestHomeworkTaskDto requestHomeworkTaskDto) {
        ResponseHomeworkTaskDto updatedHomeworkTask = authorHomeworkTaskService.updateTask(courseId, lessonId, id, requestHomeworkTaskDto);
        return ResponseEntity.ok(updatedHomeworkTask);
    }

    @DeleteMapping("/{courseId}/{lessonId}/{id}")
    @Operation(summary = "Удалить задание по ID")
    @ApiResponse(responseCode = "200", description = "Успешное удаление задания")
    public void deleteTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                           @PathVariable Long id) {
        authorHomeworkTaskService.deleteTask(courseId, lessonId, id);
    }
}
