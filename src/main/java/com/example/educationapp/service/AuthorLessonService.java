package com.example.educationapp.service;

import com.example.educationapp.dto.LessonDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorLessonService {
    private final LessonRepo lessonRepo;

    private final CourseUtils courseUtils;

    private final LessonMapper lessonMapper;

    public List<LessonDto> getAllLessons(Long courseId) {
        Course course = courseUtils.getValidatedCourse(courseId);

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());

    }

    public LessonDto createLesson(Long courseId, LessonDto lessonDto) {
        Course course = courseUtils.getValidatedCourse(courseId);
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setLessonsCourse(course);
        lesson = lessonRepo.save(lesson);
        return lessonMapper.toDto(lesson);
    }

    public LessonDto getLesson(Long courseId, Long id) {
        courseUtils.validateCourse(courseId);

        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        return lessonMapper.toDto(lesson);
    }

    public LessonDto updateLesson(Long courseId, Long id, LessonDto lessonDto) {
        courseUtils.validateCourse(courseId);

        LessonStatus newStatus = lessonDto.getStatus();

        if (!isStatusChangeValid(newStatus)) {
            throw new InvalidStatusException("Invalid status change.");
        }

        lessonDto.setId(id);
        Lesson updatedLesson = lessonMapper.toEntity(lessonDto);
        lessonRepo.save(updatedLesson);
        return lessonDto;
    }

    public void deleteLesson(Long courseId, Long id) {
        courseUtils.validateCourse(courseId);
        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        if (lesson.getStatus() != LessonStatus.NOT_ACTIVE) {
            throw new InvalidStatusException("Lesson can only be deleted if it's in NOT_ACTIVE status.");
        }

        lessonRepo.delete(lesson);
    }

    private boolean isStatusChangeValid(LessonStatus newStatus) {
        if(newStatus == LessonStatus.ACTIVE) return true;
        else return false;
    }
}
