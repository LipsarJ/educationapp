package com.example.educationapp.service;

import com.example.educationapp.dto.HomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.HomeworkTaskNotFoundException;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorHomeworkTaskService {
    private final HomeworkTaskRepo homeworkTaskRepo;

    private final HomeworkTaskMapper homeworkTaskMapper;

    private final CourseUtils courseUtils;

    private final LessonRepo lessonRepo;

    public List<HomeworkTaskDto> getAllTasks(Long courseId, Long lessonId) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        List<HomeworkTask> tasks = lesson.getHomeworkTaskList();
        return tasks.stream()
                .map(homeworkTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public HomeworkTaskDto createTask(Long courseId, Long lessonId, HomeworkTaskDto homeworkTaskDto) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));

        HomeworkTask homeworkTask = homeworkTaskMapper.toEntity(homeworkTaskDto);
        homeworkTask.setLesson(lesson);
        homeworkTask = homeworkTaskRepo.save(homeworkTask);
        return homeworkTaskMapper.toDto(homeworkTask);
    }

    public HomeworkTaskDto getTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));

        return homeworkTaskMapper.toDto(homeworkTask);
    }

    public HomeworkTaskDto updateTask(Long courseId, Long lessonId, Long id, HomeworkTaskDto homeworkTaskDto) {
        courseUtils.validateAndGetCourse(courseId);

        homeworkTaskDto.setId(id);
        HomeworkTask updatedHomeworkTask = homeworkTaskMapper.toEntity(homeworkTaskDto);

        homeworkTaskRepo.save(updatedHomeworkTask);

        return homeworkTaskDto;
    }

    public void deleteTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Homework is not found."));

        homeworkTaskRepo.delete(homeworkTask);
    }
}
