package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.dto.response.UserInfoPage;
import com.example.educationapp.service.management.teacher.TeacherManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teacher/course")
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
}
