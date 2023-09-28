package com.example.educationapp.controller.author;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.service.AuthorLessonService;
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
@RequiredArgsConstructor
@RequestMapping("api/v1/author/lessons")
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorLessonController {
    private final AuthorLessonService authorLessonService;

    @GetMapping("/{courseId}")
    @Operation(summary = "Получить все уроки для курса")
    @ApiResponse(responseCode = "200", description = "Список всех уроков для курса",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseLessonDto.class)))
    public ResponseEntity<List<ResponseLessonDto>> getAllLessons(@PathVariable Long courseId) {
        List<ResponseLessonDto> lessons = authorLessonService.getAllLessons(courseId);
        return ResponseEntity.ok(lessons);
    }

    @PostMapping("/{courseId}")
    @Operation(summary = "Создать новый урок для курса")
    @ApiResponse(responseCode = "200", description = "Возвращает информацию о созданном уроке",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseLessonDto.class)))
    public ResponseEntity<ResponseLessonDto> createLesson(@PathVariable Long courseId, @RequestBody RequestLessonDto requestLessonDto) {
        return ResponseEntity.ok(authorLessonService.createLesson(courseId, requestLessonDto));
    }

    @GetMapping("/{courseId}/{id}")
    @Operation(summary = "Получить информацию о уроке по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию о уроке",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseLessonDto.class))),
            @ApiResponse(responseCode = "404", description = "Если урок не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<ResponseLessonDto> getLesson(@PathVariable Long courseId, @PathVariable Long id) {
        ResponseLessonDto responseLessonDto = authorLessonService.getLesson(courseId, id);
        return ResponseEntity.ok(responseLessonDto);
    }

    @PutMapping("/{courseId}/{id}")
    @Operation(summary = "Обновить информацию о уроке по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию об обновленном уроке",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseLessonDto.class))),
            @ApiResponse(responseCode = "404", description = "Если урок не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<ResponseLessonDto> updateLesson(@PathVariable Long courseId, @PathVariable Long id, @RequestBody RequestLessonDto requestLessonDto) {
        ResponseLessonDto updatedLessonDto = authorLessonService.updateLesson(courseId, id, requestLessonDto);
        return ResponseEntity.ok(updatedLessonDto);
    }

    @DeleteMapping("/{courseId}/{id}")
    @Operation(summary = "Удалить урок по ID")
    @ApiResponse(responseCode = "200", description = "Успешное удаление урока")
    public void deleteLesson(@PathVariable Long courseId, @PathVariable Long id) {
        authorLessonService.deleteLesson(courseId, id);
    }
}
