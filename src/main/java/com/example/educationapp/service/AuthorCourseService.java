package com.example.educationapp.service;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.CourseNotFoundException;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorCourseService {
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;
    private final CourseMapper courseMapper;

    private final UserContext userContext;


    public List<CourseDto> getAllCoursesForAuthor(String username) {
        User author = userContext.getUser();
        List<Course> courses = userRepo.findCoursesByAuthorCourseSet(author);
        return courses.stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());

    }
    public CourseDto createCourse(CourseDto courseDto){
        Course course = courseMapper.toEntity(courseDto);
        courseRepo.save(course);
        return courseDto;
    }

    public CourseDto getCourse(Long id, String username) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(id);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }
        Course course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }
        return courseMapper.toDto(course);
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto, String username) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(id);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            if (!course.getAuthors().contains(user)) {
                throw new ForbiddenException("You are not the author of this course.");
            }

            CourseStatus currentStatus = course.getStatus();
            CourseStatus newStatus = courseDto.getStatus();

            if (!isStatusChangeValid(currentStatus, newStatus)) {
                throw new IllegalArgumentException("Invalid status change.");
            }

            courseDto.setId(id); // Make sure the ID is set for the mapping
            Course updatedCourse = courseMapper.toEntity(courseDto);
            courseRepo.save(updatedCourse);

            return courseMapper.toDto(updatedCourse);
        } else {
            throw new CourseNotFoundException("Course is not found.");
        }
    }

    public void deleteCourse(Long id, String username) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(id);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();

            if (!course.getAuthors().contains(user)) {
                throw new ForbiddenException("You are not the author of this course.");
            }

            if (course.getStatus() != CourseStatus.TEMPLATE) {
                throw new IllegalArgumentException("Course can only be deleted if it's in TEMPLATE status.");
            }

            courseRepo.delete(course);
        } else {
            throw new CourseNotFoundException("Course is not found.");
        }
    }
    private boolean isStatusChangeValid(CourseStatus currentStatus, CourseStatus newStatus) {
        if((currentStatus == CourseStatus.TEMPLATE && newStatus == CourseStatus.ONGOING)
                || (currentStatus == CourseStatus.ONGOING && newStatus == CourseStatus.ENDED)) { return true;}
        else { return false;}
    }
}
