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

    @Query("select l from Lesson l where l.num = :num and l.lessonsCourse = :course")
    boolean existsByNumAndCourse(Integer num, Course course);

    @Query("select l from Lesson l where l.num = :num and l.lessonsCourse = :course and l.id <> :lessonId")
    boolean existsByNumAndCourseAndIdNot(Integer num, Course course, Long lessonId);
}
