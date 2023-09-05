package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.CourseNameException;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        return courses.stream()
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ResponseCourseDto createCourse(RequestCourseDto requestCourseDto) {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        if (requestCourseDto.getCourseStatus() != null && requestCourseDto.getCourseStatus() != CourseStatus.TEMPLATE) {
            throw new InvalidStatusException("Course can only be created with Template status.");
        } else {
            requestCourseDto.setCourseStatus(CourseStatus.TEMPLATE);
        }
        if (courseRepo.existsByCourseName(requestCourseDto.getCourseName())) {
            throw new CourseNameException("Course name is already exists");
        }
        Course course = courseMapper.toEntity(requestCourseDto);
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getAuthorCourseSet().add(course);
        courseRepo.save(course);
        userRepo.save(user);
        return courseMapper.toResponseDto(course);
    }

    public ResponseCourseDto getCourse(Long id) {
        Course course = courseUtils.validateAndGetCourse(id);
        return courseMapper.toResponseDto(course);
    }

    @Transactional
    public ResponseCourseDto updateCourse(Long id, RequestCourseDto requestCourseDto) {
        Course course = courseUtils.validateAndGetCourse(id);
        if (courseRepo.existsByCourseNameAndIdNot(requestCourseDto.getCourseName(), id)) {
            throw new CourseNameException("Course name is already exists");
        }

        CourseStatus currentStatus = course.getCourseStatus();
        CourseStatus newStatus = requestCourseDto.getCourseStatus();

        if (!courseUtils.isStatusChangeValid(currentStatus, newStatus)) {
            throw new InvalidStatusException("Invalid status change.");
        }


        course.setCourseName(requestCourseDto.getCourseName());
        course.setCourseStatus(requestCourseDto.getCourseStatus());
        courseRepo.save(course);

        return courseMapper.toResponseDto(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseUtils.validateAndGetCourse(id);
        ResponseUserDto responseUserDto = userContext.getUserDto();
        if (course.getCourseStatus() != CourseStatus.TEMPLATE) {
            throw new InvalidStatusException("Course can only be deleted if it's in TEMPLATE status.");
        }
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.getAuthorCourseSet().remove(course);
        userRepo.save(user);
        for (Lesson lesson : course.getLessonList()) {
            authorLessonService.deleteLesson(id, lesson.getId());
        }
        course.getStudents().clear();
        course.getTeachers().clear();
        course.getAuthors().clear();
        course.getLessonList().clear();

        courseRepo.delete(course);
    }
}
