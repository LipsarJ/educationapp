package com.example.educationapp.service;

import com.example.educationapp.dto.HomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.HomeworkTaskNotFoundException;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.utils.CourseUtils;
import com.example.educationapp.utils.LessonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorHomeworkTaskService {
    private final HomeworkTaskRepo homeworkTaskRepo;

    private final HomeworkTaskMapper homeworkTaskMapper;

    private final CourseUtils courseUtils;

    private final LessonUtils lessonUtils;

    public List<HomeworkTaskDto> getAllTasks(Long courseId, Long lessonId) {
        courseUtils.validateCourse(courseId);

        Lesson lesson = lessonUtils.getValidatedLesson(lessonId);

        List<HomeworkTask> tasks = lesson.getHomeworkTaskList();
        return tasks.stream()
                .map(homeworkTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public HomeworkTaskDto createTask(Long courseId, Long lessonId, HomeworkTaskDto homeworkTaskDto) {
        courseUtils.validateCourse(courseId);

        Lesson lesson = lessonUtils.getValidatedLesson(lessonId);

        HomeworkTask homeworkTask = homeworkTaskMapper.toEntity(homeworkTaskDto);
        homeworkTask.setLesson(lesson);
        homeworkTaskRepo.save(homeworkTask);
        return homeworkTaskDto;
    }

    public HomeworkTaskDto getTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateCourse(courseId);

        lessonUtils.validateLesson(lessonId);

        Optional<HomeworkTask> homeworkTaskOptional = homeworkTaskRepo.findById(id);

        if(!homeworkTaskOptional.isPresent()) {
            throw new HomeworkTaskNotFoundException("Homework Task is not found");
        }

        HomeworkTask homeworkTask = homeworkTaskOptional.get();

        return homeworkTaskMapper.toDto(homeworkTask);
    }

    public HomeworkTaskDto updateTask(Long courseId, Long lessonId, Long id, HomeworkTaskDto homeworkTaskDto) {
        courseUtils.validateCourse(courseId);

        lessonUtils.validateLesson(lessonId);

        Optional<HomeworkTask> homeworkTaskOptional = homeworkTaskRepo.findById(id);

        if(!homeworkTaskOptional.isPresent()) {
            throw new HomeworkTaskNotFoundException("Homework Task is not found");
        }

        homeworkTaskDto.setId(id);
        HomeworkTask updatedHomeworkTask = homeworkTaskMapper.toEntity(homeworkTaskDto);

        homeworkTaskRepo.save(updatedHomeworkTask);

        return homeworkTaskDto;
    }

    public void deleteTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateCourse(courseId);

        lessonUtils.validateLesson(lessonId);

        Optional<HomeworkTask> homeworkTaskOptional = homeworkTaskRepo.findById(id);

        if(!homeworkTaskOptional.isPresent()) {
            throw new HomeworkTaskNotFoundException("Homework Task is not found");
        }

        HomeworkTask homeworkTask = homeworkTaskOptional.get();

        homeworkTaskRepo.delete(homeworkTask);
    }
}
