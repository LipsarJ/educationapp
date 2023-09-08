package com.example.educationapp.controller.management.teacher;

import com.example.educationapp.dto.request.teacher.RequestTeacherCheckHomeworkDto;
import com.example.educationapp.dto.response.HomeworkDoneInfoPage;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.service.management.teacher.TeacherHomeworkCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher/course")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('TEACHER')")
public class TeacherHomeworkCheckController {
    private final TeacherHomeworkCheckService teacherHomeworkCheckService;

    @GetMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}")
    public HomeworkDoneInfoPage getAllHomeworksDoneForTask(@PathVariable Long id, @PathVariable Long lessonId,
                                                           @PathVariable Long homeworkTaskId,
                                                           Pageable pageable,
                                                           @RequestParam(required = false) boolean checked
    ) {
        Page<ResponseHomeworkDoneStudentDto> homeworksDonePage =
                teacherHomeworkCheckService.getAllHomeworksDoneForTask(id, lessonId, homeworkTaskId, pageable, checked);
        return new HomeworkDoneInfoPage(
                homeworksDonePage.getContent(),
                homeworksDonePage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }

    @PutMapping("/{id}/lessons/{lessonId}/homeworks/{homeworkTaskId}/{homeworkDoneId}")
    public ResponseHomeworkDoneStudentDto setGradeToHomework (@PathVariable Long id, @PathVariable Long lessonId,
                                                              @PathVariable Long homeworkTaskId, @PathVariable Long homeworkDoneId,
                                                              @RequestBody RequestTeacherCheckHomeworkDto requestTeacherCheckHomeworkDto) {
        return teacherHomeworkCheckService.setGradeToHomeworkDone(id, lessonId, homeworkTaskId, homeworkDoneId, requestTeacherCheckHomeworkDto);
    }
}
