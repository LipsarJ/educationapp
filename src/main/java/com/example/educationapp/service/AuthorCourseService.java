package com.example.educationapp.service;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.User;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    public List<CourseDto> getAllCoursesForAuthor() {
        User user = userContext.getUser();
        List<Course> courses = userRepo.findCoursesByAuthorCourseSet(user);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());

    }
    public CourseDto createCourse(CourseDto courseDto){
        Course course = courseMapper.toEntity(courseDto);
        courseRepo.save(course);
        return courseDto;
    }

    public CourseDto getCourse(Long id) {
        Course course = courseUtils.getValidatedCourse(id);
        return courseMapper.toDto(course);
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseUtils.getValidatedCourse(id);

        CourseStatus currentStatus = course.getStatus();
        CourseStatus newStatus = courseDto.getStatus();

        if (!courseUtils.isStatusChangeValid(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status change.");
        }

        courseDto.setId(id);
        Course updatedCourse = courseMapper.toEntity(courseDto);
        courseRepo.save(updatedCourse);

        return courseMapper.toDto(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseUtils.getValidatedCourse(id);

        if (course.getStatus() != CourseStatus.TEMPLATE) {
            throw new IllegalArgumentException("Course can only be deleted if it's in TEMPLATE status.");
        }
        courseRepo.delete(course);
    }
}
