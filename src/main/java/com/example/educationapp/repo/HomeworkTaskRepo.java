package com.example.educationapp.repo;

import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkTaskRepo extends JpaRepository<HomeworkTask, Long> {
    List<HomeworkTask> findAllByLesson (Lesson lesson);

    boolean existsByTitle(String title);

    HomeworkTask findByTitle(String title);
}
