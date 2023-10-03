package com.example.educationapp.service.teacher;

import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.extend.UserNotFoundException;
import com.example.educationapp.mapper.CourseMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherCourseService {
    private final UserRepo userRepo;
    private final UserContext userContext;
    private final CourseMapper courseMapper;
    private final CourseUtils courseUtils;

    public List<ResponseCourseDto> getAllCoursesForTeacher() {
        ResponseUserDto responseUserDto = userContext.getUserDto();
        User user = userRepo.findById(responseUserDto.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        List<Course> courses = userRepo.findCoursesByTeacher(user);
        List<ResponseCourseDto> coursesDto = new ArrayList<>();
        for (Course course : courses) {
            int count = userRepo.countByStudentCourseSet(course);
            ResponseCourseDto responseCourseDto = courseMapper.toResponseDto(course);
            responseCourseDto.setCountStd(count);
            coursesDto.add(responseCourseDto);
        }
        return coursesDto;
    }

    public ResponseCourseDto getCourse(Long id) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        ResponseCourseDto responseCourseDto = courseMapper.toResponseDto(course);
        responseCourseDto.setCountStd(userRepo.countByStudentCourseSet(course));
        return responseCourseDto;
    }
}
