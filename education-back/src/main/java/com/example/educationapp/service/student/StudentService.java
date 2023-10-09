package com.example.educationapp.service.student;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.NotFoundException;
import com.example.educationapp.exception.extend.LessonNotFoundException;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.mapper.student.StudentCourseMapper;
import com.example.educationapp.repo.HomeworkDoneRepo;
import com.example.educationapp.repo.HomeworkTaskRepo;
import com.example.educationapp.repo.LessonRepo;
import com.example.educationapp.utils.CourseUtils;
import com.example.educationapp.utils.HomeworkUtils;
import com.example.educationapp.utils.LessonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final CourseUtils courseUtils;
    private final LessonUtils lessonUtils;
    private final HomeworkUtils homeworkUtils;
    private final StudentCourseMapper studentCourseMapper;
    private final HomeworkDoneRepo homeworkDoneRepo;
    private final LessonRepo lessonRepo;
    private final LessonMapper lessonMapper;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final HomeworkTaskMapper homeworkTaskMapper;

    public List<ResponseCourseDto> getAllCoursesForStudent() {
        Set<Course> courses = new HashSet<>();
        for(Course course : courseUtils.getCoursesForStudent()) {
            if(course.getCourseStatus() == CourseStatus.ONGOING) {
                courses.add(course);
            }
        }
        return courses.stream()
                .map(studentCourseMapper::toResponseCourseDto)
                .collect(Collectors.toList());
    }

    public ResponseCourseDto getCourseInfoForStudent(Long id) {
        Course course = courseUtils.validateAndGetCourseForStudent(id);
        return studentCourseMapper.toResponseCourseDto(course);
    }

    public List<ResponseLessonDto> getAllLessons(Long courseId) {
        Course course = courseUtils.validateAndGetCourseForStudent(courseId);

        List<Lesson> lessons = lessonRepo.findAllByLessonsCourse(course);
        return lessons.stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());

    }

    public ResponseLessonDto getLessonInfoForStudent(Long id, Long lessonId) {
        Lesson lesson = lessonUtils.getLessonForStudentValidatedCourse(id, lessonId);
        return lessonMapper.toResponseDto(lesson);
    }

    public List<ResponseHomeworkTaskDto> getAllTasks(Long courseId, Long lessonId) {
        courseUtils.validateAndGetCourseForStudent(courseId);

        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson is not found."));

        List<HomeworkTask> tasks = homeworkTaskRepo.findAllByLesson(lesson);
        return tasks.stream()
                .map(homeworkTaskMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ResponseHomeworkTaskDto getHomeworkTaskInfoForStudent(Long id, Long lessonId, Long homeworkTaskId) {
        HomeworkTask homeworkTask = homeworkUtils.getHomeworkTaskForStudentValidatedLesson(id, lessonId, homeworkTaskId);
        return homeworkTaskMapper.toResponseDto(homeworkTask);
    }

    public ResponseHomeworkDoneStudentDto getStudentHomeworkDone(Long id, Long lessonId, Long homeworkTaskId) {
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForStudent(id, lessonId, homeworkTaskId);
        return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
    }

    public ResponseHomeworkDoneStudentDto createHomeworkDone(Long id, Long lessonId,
                                                             Long homeworkTaskId, RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto) {
        if (!homeworkUtils.validateUniqueHomeworkDoneForTask(id, lessonId, homeworkTaskId)) {
            HomeworkDone homeworkDone = studentCourseMapper.toHomeworkDone(requestHomeworkDoneStudentDto);
            homeworkDone.setStudent(homeworkUtils.getStudentForHomeworkDone(id, lessonId, homeworkTaskId));
            homeworkDone.setTask(homeworkUtils.getHomeworkTaskForStudentValidatedLesson(id, lessonId, homeworkTaskId));
            homeworkDone.setSubmissionDate(LocalDateTime.now(ZoneOffset.UTC));
            homeworkDone.setStatus(HomeworkDoneStatus.PENDING);
            homeworkDoneRepo.save(homeworkDone);
            return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
        } else {
            throw new BadDataException("You already have solution for this task.", Errors.HWD_ALREADY_EXISTS);
        }
    }

    public ResponseHomeworkDoneStudentDto editStudentHomeworkDone(Long id, Long lessonId,
                                                                  Long homeworkTaskId, RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto) {
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForStudent(id, lessonId, homeworkTaskId);
        if (homeworkDone != null) {
            homeworkDone.setStudentDescription(requestHomeworkDoneStudentDto.getStudentDescription());
            homeworkDoneRepo.save(homeworkDone);
            return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
        } else {
            throw new NotFoundException("Solution for this task is not found");
        }
    }
}
