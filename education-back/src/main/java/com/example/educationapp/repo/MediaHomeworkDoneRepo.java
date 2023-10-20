package com.example.educationapp.repo;

import com.example.educationapp.entity.MediaHomeworkDone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaHomeworkDoneRepo extends JpaRepository<MediaHomeworkDone, UUID> {
}
