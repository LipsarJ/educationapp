package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.controlleradvice.SimpleResponse;
import com.example.educationapp.dto.request.management.teacher.AddOrRemoveStudentsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.dto.response.UserInfoPage;
import com.example.educationapp.service.management.teacher.TeacherManagementService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teacher/course")
@PreAuthorize("hasAuthority('TEACHER')")
public class TeacherManagement {
    private final TeacherManagementService teacherManagementService;


    @GetMapping("/{id}/students")
    @Operation(summary = "Получить всех студентов курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка студентов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoPage.class))),
            @ApiResponse(responseCode = "404", description = "Курс или студент не найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Введены невенрные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public UserInfoPage getAllStudentsForCourse(@PathVariable Long id, Pageable pageable) {
        Page<UserInfoDto> studentsPage = teacherManagementService.getAllStudentsForCourse(id, pageable);
        return new UserInfoPage(
                studentsPage.getContent(),
                studentsPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PutMapping("/{id}/add-students")
    @Operation(summary = "Добавить студентов к курсу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление студентов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс или студент не найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Введены невенрные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> addStudentsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        return teacherManagementService.addStudentsForCourse(id, addOrRemoveStudentsDto);
    }

    @PutMapping("/{id}/remove-students")
    @Operation(summary = "Удалить студентов из курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление студентов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Курс или студент не найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class))),
            @ApiResponse(responseCode = "400", description = "Введены невенрные данные",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SimpleResponse.class)))
    })
    public List<ResponseUserDto> removeStudentsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        return teacherManagementService.removeStudentsForCourse(id, addOrRemoveStudentsDto);
    }
}
