package com.example.educationapp.repo;

import com.example.educationapp.entity.MediaHomeworkTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaHomeworkTaskRepo extends JpaRepository<MediaHomeworkTask, UUID> {
}
