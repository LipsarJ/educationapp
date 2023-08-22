package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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


    public List<ResponseCourseDto> getAllCoursesForAuthor() {
        User user = userContext.getUser();
        List<Course> courses = userRepo.findCoursesByAuthorCourseSet(user);
        return courses.stream()
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());

    }
    public ResponseCourseDto createCourse(RequestCourseDto requestCourseDto){
        Course course = courseMapper.toEntity(requestCourseDto);
        course = courseRepo.save(course);
        return courseMapper.toResponseDto(course);
    }

    public ResponseCourseDto getCourse(Long id) {
        Course course = courseUtils.validateAndGetCourse(id);
        return courseMapper.toResponseDto(course);
    }

    public ResponseCourseDto updateCourse(Long id, RequestCourseDto requestCourseDto) {
        Course course = courseUtils.validateAndGetCourse(id);

        CourseStatus currentStatus = course.getStatus();
        CourseStatus newStatus = requestCourseDto.getCourseStatus();

        if (!courseUtils.isStatusChangeValid(currentStatus, newStatus)) {
            throw new InvalidStatusException("Invalid status change.");
        }

        requestCourseDto.setId(id);
        requestCourseDto.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC));
        Course updatedCourse = courseMapper.toEntity(requestCourseDto);
        courseRepo.save(updatedCourse);

        return courseMapper.toResponseDto(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseUtils.validateAndGetCourse(id);

        if (course.getStatus() != CourseStatus.TEMPLATE) {
            throw new InvalidStatusException("Course can only be deleted if it's in TEMPLATE status.");
        }
        courseRepo.delete(course);
    }
}
