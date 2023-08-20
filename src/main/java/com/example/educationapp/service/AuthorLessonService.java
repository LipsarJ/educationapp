package com.example.educationapp.service;

import com.example.educationapp.dto.LessonDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.CourseStatus;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.entity.LessonStatus;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        lessonRepo.save(lesson);
        return lessonDto;
    }

    public LessonDto getLesson(Long courseId, Long id) {
        courseUtils.validateCourse(courseId);

        Optional<Lesson> lessonOptional = lessonRepo.findById(id);

        if(!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson is not found.");
        }

        Lesson lesson = lessonOptional.get();

        return lessonMapper.toDto(lesson);
    }

    public LessonDto updateLesson(Long courseId, Long id, LessonDto lessonDto) {
        courseUtils.validateCourse(courseId);

        Optional<Lesson> lessonOptional = lessonRepo.findById(id);

        if(!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson is not found.");
        }

        LessonStatus newStatus = lessonDto.getStatus();

        if (!isStatusChangeValid(newStatus)) {
            throw new IllegalArgumentException("Invalid status change.");
        }

        lessonDto.setId(id);
        Lesson updatedLesson = lessonMapper.toEntity(lessonDto);
        lessonRepo.save(updatedLesson);
        return lessonDto;
    }

    public void deleteLesson(Long courseId, Long id) {
        courseUtils.validateCourse(courseId);
        Optional<Lesson> lessonOptional = lessonRepo.findById(id);

        if(!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson is not found.");
        }

        Lesson lesson = lessonOptional.get();

        if (lesson.getStatus() != LessonStatus.ACTIVE) {
            throw new IllegalArgumentException("Lesson can only be deleted if it's in ACTIVE status.");
        }
        lessonRepo.delete(lesson);
    }

    private boolean isStatusChangeValid(LessonStatus newStatus) {
        if(newStatus == LessonStatus.ACTIVE) return true;
        else return false;
    }
}
