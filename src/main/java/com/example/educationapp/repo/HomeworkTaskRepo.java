package com.example.educationapp.repo;

import com.example.educationapp.entity.HomeworkTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkTaskRepo extends JpaRepository<HomeworkTask, Long> {
}
