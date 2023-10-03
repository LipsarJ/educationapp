package com.example.educationapp.controller.teacher;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.service.teacher.TeacherLessonService;
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
@RequestMapping("/api/v1/teacher/lessons")
@PreAuthorize("hasAuthority('TEACHER')")
@RequiredArgsConstructor
public class TeacherLessonController {
    private final TeacherLessonService teacherLessonService;

    @GetMapping("/{courseId}")
    @Operation(summary = "Получить все уроки для курса")
    @ApiResponse(responseCode = "200", description = "Список всех уроков для курса",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseLessonDto.class)))
    public ResponseEntity<List<ResponseLessonDto>> getAllLessons(@PathVariable Long courseId) {
        List<ResponseLessonDto> lessons = teacherLessonService.getAllLessons(courseId);
        return ResponseEntity.ok(lessons);
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
        ResponseLessonDto responseLessonDto = teacherLessonService.getLesson(courseId, id);
        return ResponseEntity.ok(responseLessonDto);
    }
}
