package com.example.educationapp.service.teacher;

import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.exception.extend.LessonNotFoundException;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherLessonService {
    private final CourseUtils courseUtils;
    private final LessonRepo lessonRepo;
    private final LessonMapper lessonMapper;

    public List<ResponseLessonDto> getAllLessons(Long courseId) {
        Course course = courseUtils.validateAndGetCourseForTeacher(courseId);

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());

    }

    public ResponseLessonDto getLesson(Long courseId, Long id) {
        courseUtils.validateAndGetCourseForTeacher(courseId);

        Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        return lessonMapper.toResponseDto(lesson);
    }
}
