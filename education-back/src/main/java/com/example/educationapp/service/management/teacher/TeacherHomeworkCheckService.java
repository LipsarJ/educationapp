package com.example.educationapp.service.management.teacher;

import com.example.educationapp.dto.request.teacher.RequestTeacherCheckHomeworkDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.extend.UserNotFoundException;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.mapper.student.StudentCourseMapper;
import com.example.educationapp.repo.HomeworkDoneRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.HomeworkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherHomeworkCheckService {
    private final HomeworkUtils homeworkUtils;
    private final UserMapper userMapper;
    private final StudentCourseMapper studentCourseMapper;
    private final HomeworkDoneRepo homeworkDoneRepo;
    private final UserContext userContext;
    private final UserRepo userRepo;

    public Page<ResponseHomeworkDoneStudentDto> getAllHomeworksDoneForTask(Long id, Long lessonId,
                                                                           Long homeworkTaskId, Pageable pageable, Boolean checked) {
        HomeworkTask homeworkTask = homeworkUtils.getHomeworkTaskForTeacherValidatedLesson(id, lessonId, homeworkTaskId);
        Page<HomeworkDone> homeworkDonePage;
        if (checked == null) {
            homeworkDonePage = homeworkDoneRepo.findAllByTask(homeworkTask, pageable);
        } else if (checked) {
            homeworkDonePage = homeworkDoneRepo.findAllByTaskAndGradeIsNotNull(homeworkTask, pageable);
        } else {
            homeworkDonePage = homeworkDoneRepo.findAllByTaskAndGradeIsNull(homeworkTask, pageable);
        }

        return homeworkDonePage.map(homeworkDone -> new ResponseHomeworkDoneStudentDto(homeworkDone.getId(), homeworkDone.getSubmissionDate(),
                homeworkDone.getGrade(), homeworkDone.getStudentDescription(),
                homeworkDone.getTeacherFeedback(), userMapper.toUserInfoDto(homeworkDone.getTeacher()), userMapper.toUserInfoDto(homeworkDone.getStudent())));
    }

    public ResponseHomeworkDoneStudentDto getHomeworkDoneForTask(Long id, Long lessonId,
                                                                 Long homeworkTaskId, Long homeworkDoneId) {
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForTeacher(id, lessonId, homeworkTaskId, homeworkDoneId);
        return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
    }

    public ResponseHomeworkDoneStudentDto setGradeToHomeworkDone(Long id, Long lessonId, Long homeworkTaskId,
                                                                 Long homeworkDoneId, RequestTeacherCheckHomeworkDto requestTeacherCheckHomeworkDto) {
        ResponseUserDto responseTeacherDto = userContext.getUserDto();
        User teacher = userRepo.findById(responseTeacherDto.getId()).orElseThrow(() -> new UserNotFoundException("User is not found"));
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForTeacher(id, lessonId, homeworkTaskId, homeworkDoneId);
        homeworkDone.setGrade(requestTeacherCheckHomeworkDto.getGrade());
        homeworkDone.setTeacherFeedback(requestTeacherCheckHomeworkDto.getTeacherFeedback());
        homeworkDone.setTeacher(teacher);
        homeworkDoneRepo.save(homeworkDone);
        return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
    }
}
