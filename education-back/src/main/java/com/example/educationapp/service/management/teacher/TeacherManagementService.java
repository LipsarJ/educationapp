package com.example.educationapp.service.management.teacher;

import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class TeacherManagementService {
    private final CourseUtils courseUtils;
    private final UserRepo userRepo;
    public Page<UserInfoDto> getAllStudentsForCourse(Long id, Pageable pageable) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Page<User> studentsPage = userRepo.findAllByStudentCourseSet(Collections.singleton(course), pageable);
        return studentsPage.map(user -> new UserInfoDto(user.getId(), user.getUsername(), user.getFirstname(), user.getMiddlename(), user.getLastname()));
    }
}
