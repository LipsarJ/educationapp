package com.example.educationapp.repo.specification;

import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkDoneRepo extends JpaRepository<HomeworkDone, Long> {
    HomeworkDone findByStudentAndTask(User student, HomeworkTask homeworkTask);
}
