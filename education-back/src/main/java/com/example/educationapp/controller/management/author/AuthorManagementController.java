package com.example.educationapp.controller.management.author;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.request.management.author.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.request.management.author.AddOrRemoveTeachersDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.service.management.author.AuthorManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/author/courses")
@PreAuthorize("hasAuthority('AUTHOR')")
public class AuthorManagementController {
    private final AuthorManagementService authorManagementService;

    @GetMapping("/{id}/authors")
    @Operation(summary = "Получить всех авторов курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка авторов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> getAuthorsForCourse(@PathVariable Long id) {
        return authorManagementService.getAllAuthorsForCourse(id);
    }

    @PutMapping("/{id}/add-authors")
    @Operation(summary = "Добавить авторов к курсу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление авторов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> addAuthorsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        return authorManagementService.addAuthorsForCourse(id, addOrRemoveAuthorsDto);
    }

    @PutMapping("/{id}/remove-authors")
    @Operation(summary = "Удалить авторов из курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление авторов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> removeAuthorsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        return authorManagementService.removeAuthorsForCourse(id, addOrRemoveAuthorsDto);
    }

    @GetMapping("/{id}/teachers")
    @Operation(summary = "Получить всех учителей курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка учителей",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> getTeachersForCourse(@PathVariable Long id) {
        return authorManagementService.getAllTeachersForCourse(id);
    }

    @PutMapping("/{id}/add-teachers")
    @Operation(summary = "Добавить учителей к курсу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление учителей",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> addTeachersForCourse(@PathVariable Long id, @RequestBody AddOrRemoveTeachersDto addOrRemoveTeachersDto) {
        return authorManagementService.addTeachersForCourse(id, addOrRemoveTeachersDto);
    }

    @PutMapping("/{id}/remove-teachers")
    @Operation(summary = "Удалить учителей из курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление учителей",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> removeTeachersForCourse(@PathVariable Long id, @RequestBody AddOrRemoveTeachersDto addOrRemoveTeachersDto) {
        return authorManagementService.removeTeachersForCourse(id, addOrRemoveTeachersDto);
    }
}
