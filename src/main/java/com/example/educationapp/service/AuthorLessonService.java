package com.example.educationapp.service;

import com.example.educationapp.dto.LessonDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.CourseNotFoundException;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorLessonService {
    private final LessonRepo lessonRepo;

    private final LessonMapper lessonMapper;

    private final CourseRepo courseRepo;

    private final UserContext userContext;

    public List<LessonDto> getAllLessons(Long courseId) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }
        Course course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());

    }

    public LessonDto createLesson(Long courseId, LessonDto lessonDto) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }
        Course course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson.setLessonsCourse(course);
        lessonRepo.save(lesson);
        return lessonDto;
    }

    public LessonDto getLesson(Long courseId, Long id) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }
        Course course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }

        Optional<Lesson> lessonOptional = lessonRepo.findById(id);

        if(!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson is not found.");
        }

        Lesson lesson = lessonOptional.get();

        return lessonMapper.toDto(lesson);
    }

    public LessonDto updateLesson(Long courseId, Long id, LessonDto lessonDto) {
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }
        Course course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }

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
        User user = userContext.getUser();

        Optional<Course> courseOptional = courseRepo.findById(courseId);

        if (!courseOptional.isPresent()) {
            throw new CourseNotFoundException("Course is not found.");
        }
        Course course = courseOptional.get();

        if (!course.getAuthors().contains(user)) {
            throw new ForbiddenException("You are not the author of this course.");
        }
        Optional<Lesson> lessonOptional = lessonRepo.findById(id);

        if(!lessonOptional.isPresent()) {
            throw new LessonNotFoundException("Lesson is not found.");
        }

        Lesson lesson = lessonOptional.get();
        lessonRepo.delete(lesson);
    }

    private boolean isStatusChangeValid(LessonStatus newStatus) {
        if(newStatus == LessonStatus.ACTIVE) return true;
        else return false;
    }
}
