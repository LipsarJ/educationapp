package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
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

    public List<ResponseHomeworkTaskDto> getAllTasks(Long courseId, Long lessonId) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        List<HomeworkTask> tasks = homeworkTaskRepo.findAllByLesson(lesson);
        return tasks.stream()
                .map(homeworkTaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseHomeworkTaskDto createTask(Long courseId, Long lessonId, RequestHomeworkTaskDto requestHomeworkTaskDto) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));

        HomeworkTask homeworkTask = homeworkTaskMapper.toEntity(requestHomeworkTaskDto);
        homeworkTask.setLesson(lesson);
        homeworkTask = homeworkTaskRepo.save(homeworkTask);
        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }

    public ResponseHomeworkTaskDto getTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));

        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }

    public ResponseHomeworkTaskDto updateTask(Long courseId, Long lessonId, Long id, RequestHomeworkTaskDto requestHomeworkTaskDto) {
        courseUtils.validateAndGetCourse(courseId);

        HomeworkTask updatedHomeworkTask = homeworkTaskMapper.toEntity(requestHomeworkTaskDto);

        homeworkTaskRepo.save(updatedHomeworkTask);

        ResponseHomeworkTaskDto responseHomeworkTaskDto = homeworkTaskMapper.toResponseDto(updatedHomeworkTask);
        responseHomeworkTaskDto.setId(id);

        return responseHomeworkTaskDto;
    }

    public void deleteTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Homework is not found."));

        homeworkTaskRepo.delete(homeworkTask);
    }
}
