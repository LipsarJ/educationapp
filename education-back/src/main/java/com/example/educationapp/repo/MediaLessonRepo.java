package com.example.educationapp.repo;

import com.example.educationapp.entity.MediaLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaLessonRepo extends JpaRepository<MediaLesson, UUID> {
}
