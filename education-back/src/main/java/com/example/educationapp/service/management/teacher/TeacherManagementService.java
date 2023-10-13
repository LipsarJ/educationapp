package com.example.educationapp.service.management.teacher;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.management.teacher.AddOrRemoveStudentsDto;
import com.example.educationapp.dto.response.HomeworkPercentageProjection;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherManagementService {
    private final CourseUtils courseUtils;
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    public Page<UserInfoDto> getAllStudentsForCourse(Long id, Pageable pageable) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);

        Page<User> studentsPage = userRepo.findByCourse(course, pageable);

        return studentsPage.map(user -> new UserInfoDto(user.getId(), user.getUsername(), user.getFirstname(), user.getMiddlename(), user.getLastname()));
    }

    public List<ResponseUserDto> addStudentsForCourse(Long id, AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Set<User> students = userRepo.findByIdIn(addOrRemoveStudentsDto.getIds());
        for (User student : students) {
            if (!student.getStudentCourseSet().contains(course)) {
                student.getStudentCourseSet().add(course);
                userRepo.save(student);
            } else {
                throw new BadDataException(String.format("User with id: %s is already student of this course", student.getId()), Errors.STUDENT_ALREADY_EXISTS);
            }
        }
        return students.stream()
                .map(userMapper::toResponseUserDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUserDto> removeStudentsForCourse(Long id, AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Set<User> students = userRepo.findByIdIn(addOrRemoveStudentsDto.getIds());
        for (User student : students) {
            if (student.getStudentCourseSet().contains(course)) {
                student.getStudentCourseSet().remove(course);
                userRepo.save(student);
            } else {
                throw new ForbiddenException(String.format("User with id: %s is not student of this course", student.getId()));
            }
        }
        return students.stream()
                .map(userMapper::toResponseUserDto)
                .collect(Collectors.toList());
    }

    public List<HomeworkPercentageProjection> getHomeworkDonePercentage(Long id, Pageable pageable) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Page<User> students = userRepo.findByCourse(course, pageable);
        List<User> studentsList = students.toList();
        List<HomeworkPercentageProjection> results = userRepo.getHomeworkPercentageForCourse(course, studentsList);

        return results;
    }
}
