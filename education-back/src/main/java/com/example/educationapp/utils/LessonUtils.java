package com.example.educationapp.utils;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.exception.extend.LessonNotFoundException;
import com.example.educationapp.repo.LessonRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LessonUtils {
    private final CourseUtils courseUtils;
    private final LessonRepo lessonRepo;

    public Lesson getLessonForStudentValidatedCourse(Long id, Long lessonId) {
        Course course = courseUtils.validateAndGetCourseForStudent(id);
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found"));
        if (!course.getLessonList().contains(lesson)) {
            throw new ForbiddenException("This lesson is not for this course");
        }
        return lesson;
    }

    public Lesson getLessonForTeacherValidatedCourse(Long id, Long lessonId) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found"));
        if (!course.getLessonList().contains(lesson)) {
            throw new ForbiddenException("This lesson is not for this course");
        }
        return lesson;
    }

    public Lesson getLessonForAuthorValidatedCourse(Long id, Long lessonId) {
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found"));
        if (!course.getLessonList().contains(lesson)) {
            throw new ForbiddenException("This lesson is not for this course");
        }
        return lesson;
    }

    public void validateAllUsersForCourse(Long id) {
        courseUtils.validateCourseForAll(id);
    }
}
