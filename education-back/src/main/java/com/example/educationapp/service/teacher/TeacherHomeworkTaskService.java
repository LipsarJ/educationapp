package com.example.educationapp.service.teacher;

import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.extend.HomeworkTaskNotFoundException;
import com.example.educationapp.exception.extend.LessonNotFoundException;
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
public class TeacherHomeworkTaskService {
    private final CourseUtils courseUtils;
    private final LessonRepo lessonRepo;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final HomeworkTaskMapper homeworkTaskMapper;

    public List<ResponseHomeworkTaskDto> getAllTasks(Long courseId, Long lessonId) {
        courseUtils.validateAndGetCourseForTeacher(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        List<HomeworkTask> tasks = homeworkTaskRepo.findAllByLesson(lesson);
        return tasks.stream()
                .map(homeworkTaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseHomeworkTaskDto getTask(Long courseId, Long lessonId, Long id) {
        courseUtils.validateAndGetCourseForTeacher(courseId);

        lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new HomeworkTaskNotFoundException("Homework is not found."));

        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }
}
