package com.example.educationapp.utils;

import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.extend.CourseNotFoundException;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.exception.extend.UserNotFoundException;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class CourseUtils {
    private final UserContext userContext;
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;

    public Course validateAndGetCourseForAuthor(Long courseId) {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course is not found."));

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }
        return course;
    }

    public Course validateAndGetCourseForTeacher(Long courseId) {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course is not found."));

        if (!course.getTeachers().contains(user)) {
            throw new ForbiddenException("You are not the teacher of this course.");
        }
        return course;
    }

    public Set<Course> getCoursesForStudent() {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getStudentCourseSet();
    }

    public Course validateAndGetCourseForStudent(Long id) {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Course course = courseRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course is not found."));
        if(!user.getStudentCourseSet().contains(course)){
            throw new ForbiddenException("You are not a student of this course.");
        }
        return course;
    }
    public Course validateCourseForAll(Long id) {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Course course = courseRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course is not found."));
        if(user.getStudentCourseSet().contains(course) || user.getTeacherCourseSet().contains(course) || user.getTeacherCourseSet().contains(course)){
            throw new ForbiddenException("You are not allowed to this course.");
        }
        return course;
    }


    public boolean isStatusChangeValid(CourseStatus currentStatus, CourseStatus newStatus) {
        return (currentStatus == CourseStatus.TEMPLATE && newStatus == CourseStatus.ONGOING)
                || (currentStatus == CourseStatus.ONGOING && newStatus == CourseStatus.ENDED)
                || currentStatus == newStatus;
    }
}
