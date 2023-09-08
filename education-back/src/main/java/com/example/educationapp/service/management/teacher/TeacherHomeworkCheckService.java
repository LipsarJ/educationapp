package com.example.educationapp.service.management.teacher;

import com.example.educationapp.dto.request.teacher.RequestTeacherCheckHomeworkDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.mapper.student.StudentCourseMapper;
import com.example.educationapp.repo.specification.HomeworkDoneRepo;
import com.example.educationapp.utils.HomeworkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherHomeworkCheckService {
    private final HomeworkUtils homeworkUtils;
    private final UserMapper userMapper;
    private final StudentCourseMapper studentCourseMapper;
    private final HomeworkDoneRepo homeworkDoneRepo;

    public Page<ResponseHomeworkDoneStudentDto> getAllHomeworksDoneForTask(Long id, Long lessonId,
                                                                           Long homeworkTaskId, Pageable pageable, boolean checked) {
        HomeworkTask homeworkTask = homeworkUtils.getHomeworkTaskForTeacherValidatedLesson(id, lessonId, homeworkTaskId);
        List<HomeworkDone> homeworkDoneList = new ArrayList<>(homeworkTask.getHomeworkDoneList());

        if (!checked) {
            Iterator<HomeworkDone> iterator = homeworkDoneList.iterator();
            while (iterator.hasNext()) {
                HomeworkDone homeworkDone = iterator.next();
                if (homeworkDone.getGrade() != null) {
                    iterator.remove();
                }
            }
        }

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<HomeworkDone> pageHomeworkDone;

        if (startItem < homeworkDoneList.size()) {
            int toIndex = Math.min(startItem + pageSize, homeworkDoneList.size());
            pageHomeworkDone = homeworkDoneList.subList(startItem, toIndex);
        } else {
            pageHomeworkDone = Collections.emptyList();
        }

        return new PageImpl<>(
                pageHomeworkDone.stream()
                        .map(homeworkDone -> new ResponseHomeworkDoneStudentDto(homeworkDone.getId(), homeworkDone.getSubmissionDate(), homeworkDone.getGrade(),
                                homeworkDone.getStudentDescription(), homeworkDone.getTeacherFeedback(), userMapper.toUserInfoDto(homeworkDone.getTeacher())))
                        .collect(Collectors.toList()),
                pageable,
                homeworkDoneList.size()
        );
    }

    public ResponseHomeworkDoneStudentDto setGradeToHomeworkDone(Long id, Long lessonId, Long homeworkTaskId,
                                                                 Long homeworkDoneId, RequestTeacherCheckHomeworkDto requestTeacherCheckHomeworkDto) {
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForTeacher(id, lessonId, homeworkTaskId, homeworkDoneId);
        homeworkDone.setGrade(requestTeacherCheckHomeworkDto.getGrade());
        homeworkDone.setTeacherFeedback(requestTeacherCheckHomeworkDto.getTeacherFeedback());
        homeworkDoneRepo.save(homeworkDone);
        return studentCourseMapper.toResponseHomeworkDoneDto(homeworkDone);
    }
}
