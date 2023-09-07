package com.example.educationapp.controller.student;

import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseCourseStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkTaskStudentDto;
import com.example.educationapp.dto.response.student.ResponseLessonStudentDto;
import com.example.educationapp.service.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/courses")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<ResponseCourseStudentDto> getAllStudentsForStudent() {
        return studentService.getAllCoursesForStudent();
    }

    @GetMapping("/{id}")
    public ResponseCourseStudentDto getCourseInfoForStudent(@PathVariable Long id) {
        return studentService.getCourseInfoForStudent(id);
    }

    @GetMapping("/{id}/lessons/{lessonId}")
    public ResponseLessonStudentDto getLessonInfoForStudent(@PathVariable Long id, @PathVariable Long lessonId) {
        return studentService.getLessonInfoForStudent(id, lessonId);
    }

    @GetMapping("{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}")
    public ResponseHomeworkTaskStudentDto getHomeworkTaskInfoForStudent(@PathVariable Long id, @PathVariable Long lessonId,
                                                                        @PathVariable Long homeworkTaskId) {
        return studentService.getHomeworkTaskInfoForStudent(id, lessonId, homeworkTaskId);
    }

    @GetMapping("{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/my-homework")
    public ResponseHomeworkDoneStudentDto getStudentHomeworkDone(@PathVariable Long id, @PathVariable Long lessonId,
                                                                  @PathVariable Long homeworkTaskId) {
        return studentService.getStudentsHomeworkDone(id, lessonId, homeworkTaskId);
    }

    @PostMapping("{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/my-homework")
    public ResponseHomeworkDoneStudentDto createStudentHomeworkDone(@PathVariable Long id, @PathVariable Long lessonId,
                                                                    @PathVariable Long homeworkTaskId, @RequestBody RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto) {
        return studentService.createHomeworkDone(id, lessonId, homeworkTaskId, requestHomeworkDoneStudentDto);
    }
}
