package com.example.educationapp.utils;

import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.HomeworkTaskNotFoundException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.repo.specification.HomeworkDoneRepo;
import com.example.educationapp.security.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HomeworkUtils {
    private final LessonUtils lessonUtils;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final UserContext userContext;
    private final UserRepo userRepo;
    private final HomeworkDoneRepo homeworkDoneRepo;

    public HomeworkTask getHomeworkTaskForValidatedLesson(Long id, Long lessonId, Long homeworkTaskId) {
        Lesson lesson = lessonUtils.getLessonForValidatedCourse(id, lessonId);
        HomeworkTask homeworkTask = homeworkTaskRepo.findById(homeworkTaskId).orElseThrow(() -> new HomeworkTaskNotFoundException("HomeworkTask is not found"));
        if (!lesson.getHomeworkTaskList().contains(homeworkTask)) {
            throw new BadDataException("This homework task is not for this lesson");
        }
        return homeworkTask;
    }

    public HomeworkDone getHomeworkDoneForStudent(Long id, Long lessonId, Long homeworkTaskId) {
        ResponseUserDto studentDto = userContext.getUserDto();
        User student = userRepo.findById(studentDto.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
        HomeworkTask homeworkTask = getHomeworkTaskForValidatedLesson(id, lessonId, homeworkTaskId);
        HomeworkDone homeworkDone = homeworkDoneRepo.findByStudentAndTask(student, homeworkTask);
        return homeworkDone;
    }

    public User getStudentForHomeworkDone(Long id, Long lessonId, Long homeworkTaskId) {
        ResponseUserDto studentDto = userContext.getUserDto();
        User student = userRepo.findById(studentDto.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
        getHomeworkTaskForValidatedLesson(id, lessonId, homeworkTaskId);
        return student;
    }

    public boolean validateUniqueHomeworkDoneForTask(Long id, Long lessonId, Long homeworkTaskId) {
        return getHomeworkDoneForStudent(id, lessonId, homeworkTaskId) != null;
    }
}
