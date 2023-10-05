package com.example.educationapp.controller.teacher;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.service.teacher.TeacherCourseService;
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
@RequestMapping("/api/v1/teacher/courses")
@PreAuthorize("hasAuthority('TEACHER')")
@RequiredArgsConstructor
public class TeacherCourseController {
    private final TeacherCourseService teacherCourseService;

    @GetMapping
    @Operation(summary = "Получить все курсы для учителя")
    @ApiResponse(responseCode = "200", description = "Список всех курсов для учителя",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseCourseDto.class)))
    public ResponseEntity<List<ResponseCourseDto>> getAllCoursesForAuthor() {
        List<ResponseCourseDto> courses = teacherCourseService.getAllCoursesForTeacher();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о курсе по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию о курсе",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseCourseDto.class))),
            @ApiResponse(responseCode = "404", description = "Если курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<ResponseCourseDto> getCourse(@PathVariable Long id) {
        ResponseCourseDto responseCourseDto = teacherCourseService.getCourse(id);
        return ResponseEntity.ok(responseCourseDto);
    }
}
