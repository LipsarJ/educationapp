package com.example.educationapp.utils;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.LessonNotFoundException;
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
        if(!course.getLessonList().contains(lesson)) {
            throw new BadDataException("This lesson is not for this course");
        }
        return lesson;
    }

    public Lesson getLessonForTeacherValidatedCourse(Long id, Long lessonId) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found"));
        if(!course.getLessonList().contains(lesson)) {
            throw new BadDataException("This lesson is not for this course");
        }
        return lesson;
    }
}
