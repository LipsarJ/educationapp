package com.example.educationapp.repo;

import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkDoneRepo extends JpaRepository<HomeworkDone, Long> {
    HomeworkDone findByStudentAndTask(User student, HomeworkTask homeworkTask);

    List<HomeworkDone> findAllByTaskAndGradeIsNull(HomeworkTask homeworkTask);

    List<HomeworkDone> findAllByTaskAndGradeIsNotNull(HomeworkTask homeworkTask);
}
