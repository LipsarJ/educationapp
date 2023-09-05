package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.MediaHomeworkTask;
import com.example.educationapp.exception.HomeworkTaskNameException;
import com.example.educationapp.exception.HomeworkTaskNotFoundException;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.MediaHomeworkTaskRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorHomeworkTaskService {
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final HomeworkTaskMapper homeworkTaskMapper;
    private final CourseUtils courseUtils;
    private final LessonRepo lessonRepo;
    private final MediaHomeworkTaskRepo mediaHomeworkTaskRepo;

    public List<ResponseHomeworkTaskDto> getAllTasks(Long courseId, Long lessonId) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        List<HomeworkTask> tasks = homeworkTaskRepo.findAllByLesson(lesson);
        return tasks.stream()
                .map(homeworkTaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseHomeworkTaskDto createTask(Long courseId, Long lessonId, RequestHomeworkTaskDto requestHomeworkTaskDto) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        if (homeworkTaskRepo.existsByTitle(requestHomeworkTaskDto.getTitle())) {
            throw new HomeworkTaskNameException("Homework Task with this name is already exists.");
        }

        HomeworkTask homeworkTask = homeworkTaskMapper.toEntity(requestHomeworkTaskDto);
        homeworkTask.setLesson(lesson);
        homeworkTask = homeworkTaskRepo.save(homeworkTask);
        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }

    public ResponseHomeworkTaskDto getTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));

        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }

    @Transactional
    public ResponseHomeworkTaskDto updateTask(Long courseId, Long lessonId, Long id, RequestHomeworkTaskDto requestHomeworkTaskDto) {
        courseUtils.validateAndGetCourse(courseId);

        lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework Task is not found"));

        if (homeworkTaskRepo.existsByTitleAndIdNot(requestHomeworkTaskDto.getTitle(), id)) {
            throw new HomeworkTaskNameException("Homework task with this name is already exists.");
        }
        HomeworkTask updatedHomeworkTask = homeworkTaskMapper.toEntity(requestHomeworkTaskDto);
        homeworkTask.setTitle(updatedHomeworkTask.getTitle());
        homeworkTask.setDescription(updatedHomeworkTask.getDescription());
        homeworkTask.setDeadlineDate(updatedHomeworkTask.getDeadlineDate());
        homeworkTaskRepo.save(homeworkTask);

        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }

    @Transactional
    public void deleteTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourse(courseId);
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));
        for (MediaHomeworkTask mediaHomeworkTask : homeworkTask.getHomeworkTaskMediaList()) {
            mediaHomeworkTaskRepo.delete(mediaHomeworkTask);
        }
        lesson.getHomeworkTaskList().remove(homeworkTask);
        lessonRepo.save(lesson);
        homeworkTask.getHomeworkTaskMediaList().clear();
        homeworkTaskRepo.delete(homeworkTask);
    }
}
