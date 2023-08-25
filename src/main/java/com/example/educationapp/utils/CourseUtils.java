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
@RequiredArgsConstructor
@Component
public class CourseUtils {
    private final UserContext userContext;
    private final CourseRepo courseRepo;

    public Course validateAndGetCourse(Long courseId) {
        User user = userContext.getUser();

        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course is not found."));

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }
        return course;
    }

    public boolean isStatusChangeValid(CourseStatus currentStatus, CourseStatus newStatus) {
        if((currentStatus == CourseStatus.TEMPLATE && newStatus == CourseStatus.ONGOING)
                || (currentStatus == CourseStatus.ONGOING && newStatus == CourseStatus.ENDED)) { return true;}
        else if (currentStatus == newStatus){ return true;}
        else {return false;}
    }
}
