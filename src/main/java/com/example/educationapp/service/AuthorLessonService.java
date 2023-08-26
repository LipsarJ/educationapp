package com.example.educationapp.service;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.InvalidStatusException;
import com.example.educationapp.exception.LessonNameException;
import com.example.educationapp.exception.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.repo.MediaLessonRepo;
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

    private final CourseRepo courseRepo;

    private final HomeworkTaskRepo homeworkTaskRepo;

    private final MediaLessonRepo mediaLessonRepo;

    public List<ResponseLessonDto> getAllLessons(Long courseId) {
        Course course = courseUtils.validateAndGetCourse(courseId);

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());

    }

    public ResponseLessonDto createLesson(Long courseId, RequestLessonDto requestLessonDto) {
        Course course = courseUtils.validateAndGetCourse(courseId);
        if(requestLessonDto.getLessonStatus() == LessonStatus.NOT_ACTIVE) {
            throw new InvalidStatusException("Lesson can be only created with Active status.");
        } else {
            requestLessonDto.setLessonStatus(LessonStatus.ACTIVE);
        }
        if(lessonRepo.existsByLessonName(requestLessonDto.getLessonName())){
            throw new LessonNameException("Lesson with this name is already exists.");
        }
        Lesson lesson = lessonMapper.toEntity(requestLessonDto);
        lesson.setLessonsCourse(course);
        lesson = lessonRepo.save(lesson);
        course.getLessonList().add(lesson);
        courseRepo.save(course);
        return lessonMapper.toResponseDto(lesson);
    }

    public ResponseLessonDto getLesson(Long courseId, Long id) {
        courseUtils.validateAndGetCourse(courseId);

        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        return lessonMapper.toResponseDto(lesson);
    }

    public ResponseLessonDto updateLesson(Long courseId, Long id, RequestLessonDto requestLessonDto) {
        courseUtils.validateAndGetCourse(courseId);
        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found"));
        LessonStatus newStatus = requestLessonDto.getLessonStatus();
        if(lessonRepo.existsByLessonNameAndIdNot(requestLessonDto.getLessonName(), id)){
            throw new LessonNameException("Lesson with this name is already exists.");
        }

        if (!isStatusChangeValid(newStatus)) {
            throw new InvalidStatusException("Invalid status change.");
        }

        lesson.setLessonName(requestLessonDto.getLessonName());
        lesson.setLessonStatus(requestLessonDto.getLessonStatus());
        lesson.setContent(requestLessonDto.getContent());
        lessonRepo.save(lesson);
        return lessonMapper.toResponseDto(lesson);
    }

    public void deleteLesson(Long courseId, Long id) {
        Course course = courseUtils.validateAndGetCourse(courseId);
        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        if (lesson.getLessonStatus() != LessonStatus.NOT_ACTIVE) {
            throw new InvalidStatusException("Lesson can only be deleted if it's in NOT_ACTIVE status.");
        }
        if(!lesson.getHomeworkTaskList().isEmpty()){
            for(HomeworkTask homeworkTask : lesson.getHomeworkTaskList()){
                homeworkTask.setLesson(null);
                homeworkTaskRepo.save(homeworkTask);
            }
        }
        if(!lesson.getMediaLessonSet().isEmpty()){
            for(MediaLesson mediaLesson : lesson.getMediaLessonSet()){
                mediaLesson.setMediaLesson(null);
                mediaLessonRepo.save(mediaLesson);
            }
        }
        course.getLessonList().remove(lesson);
        courseRepo.save(course);
        lesson.getHomeworkTaskList().clear();
        lesson.getMediaLessonSet().clear();
        lessonRepo.delete(lesson);
    }

    private boolean isStatusChangeValid(LessonStatus newStatus) {
        if(newStatus == LessonStatus.ACTIVE) return true;
        else return false;
    }
}
