package com.example.educationapp.service.author;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.extend.InvalidStatusException;
import com.example.educationapp.exception.extend.LessonNameException;
import com.example.educationapp.exception.extend.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.MediaLessonRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorLessonService {
    private final LessonRepo lessonRepo;
    private final CourseUtils courseUtils;
    private final LessonMapper lessonMapper;
    private final CourseRepo courseRepo;
    private final MediaLessonRepo mediaLessonRepo;
    private final AuthorHomeworkTaskService authorHomeworkTaskService;

    public List<ResponseLessonDto> getAllLessons(Long courseId) {
        Course course = courseUtils.validateAndGetCourseForAuthor(courseId);

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ResponseLessonDto createLesson(Long courseId, RequestLessonDto requestLessonDto) {
        Course course = courseUtils.validateAndGetCourseForAuthor(courseId);
        if (requestLessonDto.getLessonStatus() == null) {
            requestLessonDto.setLessonStatus(LessonStatus.ACTIVE);
        } else if (requestLessonDto.getLessonStatus() != LessonStatus.ACTIVE){
            throw new InvalidStatusException("Lesson can be only created with Active status.", Errors.STATUS_IS_INVALID);
        }
        if (lessonRepo.existsByLessonName(requestLessonDto.getLessonName())) {
            throw new LessonNameException("Lesson with this name is already exists.", Errors.LESSON_NAME_TAKEN);
        }
        Lesson lesson = lessonMapper.toEntity(requestLessonDto);
        lesson.setLessonsCourse(course);
        lessonRepo.save(lesson);
        return lessonMapper.toResponseDto(lesson);
    }

    public ResponseLessonDto getLesson(Long courseId, Long id) {
        courseUtils.validateAndGetCourseForAuthor(courseId);

        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        return lessonMapper.toResponseDto(lesson);
    }

    @Transactional
    public ResponseLessonDto updateLesson(Long courseId, Long id, RequestLessonDto requestLessonDto) {
        courseUtils.validateAndGetCourseForAuthor(courseId);
        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found"));
        LessonStatus newStatus;
        if (requestLessonDto.getLessonStatus() != null) {
            newStatus = requestLessonDto.getLessonStatus();
        } else {
            newStatus = lesson.getLessonStatus();
        }
        if (lessonRepo.existsByLessonNameAndIdNot(requestLessonDto.getLessonName(), id)) {
            throw new LessonNameException("Lesson with this name is already exists.", Errors.LESSON_NAME_TAKEN);
        }

        if (!isStatusChangeValid(newStatus)) {
            throw new InvalidStatusException("Invalid status change.", Errors.STATUS_IS_INVALID);
        }

        lesson.setLessonName(requestLessonDto.getLessonName());
        lesson.setLessonStatus(newStatus);
        lesson.setContent(requestLessonDto.getContent());
        lessonRepo.save(lesson);
        return lessonMapper.toResponseDto(lesson);
    }

    @Transactional
    public void deleteLesson(Long courseId, Long id) {
        Course course = courseUtils.validateAndGetCourseForAuthor(courseId);
        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        if (lesson.getLessonStatus() != LessonStatus.NOT_ACTIVE) {
            throw new InvalidStatusException("Lesson can only be deleted if it's in NOT_ACTIVE status.", Errors.STATUS_IS_INVALID);
        }
        course.getLessonList().remove(lesson);
        courseRepo.save(course);
        lesson.getHomeworkTaskList().clear();
        lesson.getMediaLessonList().clear();
        lessonRepo.delete(lesson);
    }

    private boolean isStatusChangeValid(LessonStatus newStatus) {
        return newStatus == LessonStatus.ACTIVE;
    }
}
