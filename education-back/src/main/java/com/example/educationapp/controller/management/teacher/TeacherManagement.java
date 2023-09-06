package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.dto.request.AddOrRemoveStudentsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.dto.response.UserInfoPage;
import com.example.educationapp.service.management.teacher.TeacherManagementService;
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
    public List<ResponseUserDto> addStudentsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        return teacherManagementService.addStudentsForCourse(id, addOrRemoveStudentsDto);
    }

    @PutMapping("/{id}/remove-students")
    public List<ResponseUserDto> removeStudentsForCourse(@PathVariable Long id, @RequestBody AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        return teacherManagementService.removeStudentsForCourse(id, addOrRemoveStudentsDto);
    }
}
