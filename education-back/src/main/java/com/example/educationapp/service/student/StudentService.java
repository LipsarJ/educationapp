package com.example.educationapp.service.student;

import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseCourseStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkTaskStudentDto;
import com.example.educationapp.dto.response.student.ResponseLessonStudentDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.mapper.student.StudentCourseMapper;
import com.example.educationapp.repo.HomeworkDoneRepo;
import com.example.educationapp.utils.CourseUtils;
import com.example.educationapp.utils.HomeworkUtils;
import com.example.educationapp.utils.LessonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    public List<ResponseCourseStudentDto> getAllCoursesForStudent() {
        Set<Course> courses = courseUtils.getCoursesForStudent();
        return courses.stream()
                .map(studentCourseMapper::toResponseCourseDto)
                .collect(Collectors.toList());
    }

    public ResponseCourseStudentDto getCourseInfoForStudent(Long id) {
        Course course = courseUtils.validateAndGetCourseForStudent(id);
        return studentCourseMapper.toResponseCourseDto(course);
    }

    public ResponseLessonStudentDto getLessonInfoForStudent(Long id, Long lessonId) {
        Lesson lesson = lessonUtils.getLessonForStudentValidatedCourse(id, lessonId);
        return studentCourseMapper.toResponseLessonDto(lesson);
    }

    public ResponseHomeworkTaskStudentDto getHomeworkTaskInfoForStudent(Long id, Long lessonId, Long homeworkTaskId) {
        HomeworkTask homeworkTask = homeworkUtils.getHomeworkTaskForStudentValidatedLesson(id, lessonId, homeworkTaskId);
        return studentCourseMapper.toResponseHomeworkTaskDto(homeworkTask);
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
        }
        else {
            throw new BadDataException("You already have solution for this task.");
        }
    }

    public ResponseHomeworkDoneStudentDto editStudentHomeworkDone(Long id, Long lessonId,
                                                                  Long homeworkTaskId, RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto){
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForStudent(id, lessonId, homeworkTaskId);
        if(homeworkDone != null) {
            homeworkDone.setStudentDescription(requestHomeworkDoneStudentDto.getStudentDescription());
            homeworkDoneRepo.save(homeworkDone);
            return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
        }
        else {
            throw new BadDataException("Solution for this task is not found");
        }
    }
}
