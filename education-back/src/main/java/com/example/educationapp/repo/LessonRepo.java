package com.example.educationapp.repo;

import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepo extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByLessonsCourse(Course course);

    boolean existsByLessonName(String lessonName);

    Lesson findByLessonName(String lessonName);

    boolean existsByLessonNameAndIdNot(String lessonName, Long lessonId);

    boolean existsByNumAndLessonsCourse(Integer num, Course course);

    boolean existsByNumAndLessonsCourseAndIdNot(Integer num, Course course, Long lessonId);
}
