package com.example.educationapp.service;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.extend.CourseNameException;
import com.example.educationapp.exception.extend.InvalidStatusException;
import com.example.educationapp.exception.extend.UserNotFoundException;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorCourseService {
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;
    private final CourseMapper courseMapper;
    private final UserContext userContext;
    private final CourseUtils courseUtils;
    private final AuthorLessonService authorLessonService;

    public List<ResponseCourseDto> getAllCoursesForAuthor() {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Course> courses = userRepo.findCoursesByAuthor(user);
        List<ResponseCourseDto> coursesDto = new ArrayList<>();
        for (Course course : courses) {
            int count = userRepo.countByStudentCourseSet(course);
            ResponseCourseDto responseCourseDto = courseMapper.toResponseDto(course);
            responseCourseDto.setCountStd(count);
            coursesDto.add(responseCourseDto);
        }
        return coursesDto;
    }

    @Transactional
    public ResponseCourseDto createCourse(RequestCourseDto requestCourseDto) {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        if (requestCourseDto.getCourseStatus() != null && requestCourseDto.getCourseStatus() != CourseStatus.TEMPLATE) {
            throw new InvalidStatusException("Course can only be created with Template status.", Errors.STATUS_IS_INVALID);
        } else {
            requestCourseDto.setCourseStatus(CourseStatus.TEMPLATE);
        }
        if (courseRepo.existsByCourseName(requestCourseDto.getCourseName())) {
            throw new CourseNameException("Course name is already exists", Errors.COURSE_NAME_TAKEN);
        }
        Course course = courseMapper.toEntity(requestCourseDto);
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getAuthorCourseSet().add(course);
        courseRepo.save(course);
        userRepo.save(user);
        return courseMapper.toResponseDto(course);
    }

    public ResponseCourseDto getCourse(Long id) {
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        ResponseCourseDto responseCourseDto = courseMapper.toResponseDto(course);
        responseCourseDto.setCountStd(userRepo.countByStudentCourseSet(course));
        return responseCourseDto;
    }

    @Transactional
    public ResponseCourseDto updateCourse(Long id, RequestCourseDto requestCourseDto) {
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        if (courseRepo.existsByCourseNameAndIdNot(requestCourseDto.getCourseName(), id)) {
            throw new CourseNameException("Course name is already exists", Errors.COURSE_NAME_TAKEN);
        }

        CourseStatus newStatus;
        CourseStatus currentStatus = course.getCourseStatus();

        if (requestCourseDto.getCourseStatus() == null) {
            newStatus = course.getCourseStatus();
        } else {
            newStatus = requestCourseDto.getCourseStatus();
        }

        if (!courseUtils.isStatusChangeValid(currentStatus, newStatus)) {
            throw new InvalidStatusException("Invalid status change.", Errors.STATUS_IS_INVALID);
        }


        course.setCourseName(requestCourseDto.getCourseName());
        course.setCourseStatus(newStatus);
        courseRepo.save(course);
        ResponseCourseDto responseCourseDto = courseMapper.toResponseDto(course);

        return responseCourseDto;
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        ResponseUserDto responseUserDto = userContext.getUserDto();

        if (course.getCourseStatus() != CourseStatus.TEMPLATE) {
            throw new InvalidStatusException("Course can only be deleted if it's in TEMPLATE status.", Errors.STATUS_IS_INVALID);
        }

        User user = userRepo.findById(responseUserDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getAuthorCourseSet().remove(course);
        userRepo.save(user);

        for (User student : course.getStudents()) {
            student.getStudentCourseSet().remove(course);
            userRepo.save(student);
        }

        for (User author : course.getAuthors()) {
            author.getAuthorCourseSet().remove(course);
            userRepo.save(author);
        }

        for (User teacher : course.getTeachers()) {
            teacher.getTeacherCourseSet().remove(course);
            userRepo.save(teacher);
        }

        courseRepo.delete(course);
    }
}
