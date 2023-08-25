package com.example.educationapp.repo;

import com.example.educationapp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepo extends JpaRepository<Course, Long> {

    boolean existsByCourseName(String courseName);

    boolean existsByCourseNameAndIdNot(String courseName, Long id);
}
