package com.example.educationapp.utils;

import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.HomeworkDoneNotFoundException;
import com.example.educationapp.exception.HomeworkTaskNotFoundException;
import com.example.educationapp.exception.UserNotFoundException;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.repo.specification.HomeworkDoneRepo;
import com.example.educationapp.security.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HomeworkUtils {
    private final LessonUtils lessonUtils;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final UserContext userContext;
    private final UserRepo userRepo;
    private final HomeworkDoneRepo homeworkDoneRepo;

    public HomeworkTask getHomeworkTaskForStudentValidatedLesson(Long id, Long lessonId, Long homeworkTaskId) {
        Lesson lesson = lessonUtils.getLessonForStudentValidatedCourse(id, lessonId);
        HomeworkTask homeworkTask = homeworkTaskRepo.findById(homeworkTaskId).orElseThrow(() -> new HomeworkTaskNotFoundException("HomeworkTask is not found"));
        if (!lesson.getHomeworkTaskList().contains(homeworkTask)) {
            throw new BadDataException("This homework task is not for this lesson");
        }
        return homeworkTask;
    }

    public HomeworkTask getHomeworkTaskForTeacherValidatedLesson(Long id, Long lessonId, Long homeworkTaskId) {
        Lesson lesson = lessonUtils.getLessonForTeacherValidatedCourse(id, lessonId);
        HomeworkTask homeworkTask = homeworkTaskRepo.findById(homeworkTaskId).orElseThrow(() -> new HomeworkTaskNotFoundException("HomeworkTask is not found"));
        if (!lesson.getHomeworkTaskList().contains(homeworkTask)) {
            throw new BadDataException("This homework task is not for this lesson");
        }
        return homeworkTask;
    }

    public HomeworkDone getHomeworkDoneForStudent(Long id, Long lessonId, Long homeworkTaskId) {
        ResponseUserDto studentDto = userContext.getUserDto();
        User student = userRepo.findById(studentDto.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
        HomeworkTask homeworkTask = getHomeworkTaskForStudentValidatedLesson(id, lessonId, homeworkTaskId);
        HomeworkDone homeworkDone = homeworkDoneRepo.findByStudentAndTask(student, homeworkTask);
        return homeworkDone;
    }

    public User getStudentForHomeworkDone(Long id, Long lessonId, Long homeworkTaskId) {
        ResponseUserDto studentDto = userContext.getUserDto();
        User student = userRepo.findById(studentDto.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
        getHomeworkTaskForStudentValidatedLesson(id, lessonId, homeworkTaskId);
        return student;
    }

    public HomeworkDone getHomeworkDoneForTeacher(Long id, Long lessonId, Long homeworkTaskId, Long homeworkDoneId) {
        ResponseUserDto responseTeacherDto = userContext.getUserDto();
        User teacher = userRepo.findById(responseTeacherDto.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
        Lesson lesson = lessonUtils.getLessonForTeacherValidatedCourse(id, lessonId);
        HomeworkTask homeworkTask = homeworkTaskRepo.findById(homeworkTaskId).orElseThrow(() -> new HomeworkTaskNotFoundException("HomeworkTask is not found"));
        if (!lesson.getHomeworkTaskList().contains(homeworkTask)) {
            throw new BadDataException("This homework task is not for this lesson");
        }
        HomeworkDone homeworkDone = homeworkDoneRepo.findById(homeworkDoneId).orElseThrow(() -> new HomeworkDoneNotFoundException(String.format("Homework solution with id: %s is not found", homeworkDoneId)));
        homeworkDone.setTeacher(teacher);
        return homeworkDone;
    }

    public boolean validateUniqueHomeworkDoneForTask(Long id, Long lessonId, Long homeworkTaskId) {
        return getHomeworkDoneForStudent(id, lessonId, homeworkTaskId) != null;
    }
}
