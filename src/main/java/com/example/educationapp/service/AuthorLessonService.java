package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
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

    public List<ResponseLessonDto> getAllLessons(Long courseId) {
        Course course = courseUtils.validateAndGetCourse(courseId);

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());

    }

    public ResponseLessonDto createLesson(Long courseId, RequestLessonDto requestLessonDto) {
        Course course = courseUtils.validateAndGetCourse(courseId);
        Lesson lesson = lessonMapper.toEntity(requestLessonDto);
        lesson.setLessonsCourse(course);
        lesson = lessonRepo.save(lesson);
        return lessonMapper.toResponseDto(lesson);
    }

    public ResponseLessonDto getLesson(Long courseId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        return lessonMapper.toResponseDto(lesson);
    }

    public ResponseLessonDto updateLesson(Long courseId, Long id, RequestLessonDto requestLessonDto) {
        courseUtils.validateAndGetCourse(courseId);

        LessonStatus newStatus = requestLessonDto.getLessonStatus();

        if (!isStatusChangeValid(newStatus)) {
            throw new InvalidStatusException("Invalid status change.");
        }

        requestLessonDto.setId(id);
        Lesson updatedLesson = lessonMapper.toEntity(requestLessonDto);
        lessonRepo.save(updatedLesson);
        return lessonMapper.toResponseDto(updatedLesson);
    }

    public void deleteLesson(Long courseId, Long id) {
        courseUtils.validateAndGetCourse(courseId);
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
