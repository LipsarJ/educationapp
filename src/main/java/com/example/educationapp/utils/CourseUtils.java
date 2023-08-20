package com.example.educationapp.utils;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.CourseNotFoundException;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.security.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@RequiredArgsConstructor
@Component
public class CourseUtils {

    private final UserContext userContext;

    private final CourseRepo courseRepo;

    private Course course;

    public void validateCourse(Long courseId) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }

        course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }
    }

    public Course getValidatedCourse(Long courseId) {
        validateCourse(courseId);
        return course;
    }

    public boolean isStatusChangeValid(CourseStatus currentStatus, CourseStatus newStatus) {
        if((currentStatus == CourseStatus.TEMPLATE && newStatus == CourseStatus.ONGOING)
                || (currentStatus == CourseStatus.ONGOING && newStatus == CourseStatus.ENDED)) { return true;}
        else { return false;}
    }
}
