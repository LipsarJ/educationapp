package com.example.educationapp.controller.author;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.service.AuthorCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorCourseController {
    private final AuthorCourseService authorCourseService;

    @GetMapping
    @Operation(summary = "Получить все курсы для автора")
    @ApiResponse(responseCode = "200", description = "Список всех курсов для автора",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseCourseDto.class)))
    public ResponseEntity<List<ResponseCourseDto>> getAllCoursesForAuthor() {
        List<ResponseCourseDto> courses = authorCourseService.getAllCoursesForAuthor();
        return ResponseEntity.ok(courses);
    }

    @PostMapping
    @Operation(summary = "Создать новый курс")
    @ApiResponse(responseCode = "200", description = "Возвращает информацию о созданном курсе",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseCourseDto.class)))
    public ResponseEntity<ResponseCourseDto> createCourse(@RequestBody RequestCourseDto requestCourseDto) {
        return ResponseEntity.ok(authorCourseService.createCourse(requestCourseDto));
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
        ResponseCourseDto responseCourseDto = authorCourseService.getCourse(id);
        return ResponseEntity.ok(responseCourseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить информацию о курсе по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Возвращает информацию об обновленном курсе",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseCourseDto.class))),
            @ApiResponse(responseCode = "404", description = "Если курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public ResponseEntity<ResponseCourseDto> updateCourse(@PathVariable Long id, @RequestBody RequestCourseDto requestCourseDto) {
        ResponseCourseDto updatedCourseDto = authorCourseService.updateCourse(id, requestCourseDto);
        return ResponseEntity.ok(updatedCourseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить курс по ID")
    @ApiResponse(responseCode = "200", description = "Успешное удаление курса")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        authorCourseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
}
