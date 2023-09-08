package com.example.educationapp.service.management.teacher;

import com.example.educationapp.dto.request.management.teacher.AddOrRemoveStudentsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

        List<User> studentsList = new ArrayList<>(course.getStudents());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<User> pageStudents;

        if (startItem < studentsList.size()) {
            int toIndex = Math.min(startItem + pageSize, studentsList.size());
            pageStudents = studentsList.subList(startItem, toIndex);
        } else {
            pageStudents = Collections.emptyList();
        }

        return new PageImpl<>(
                pageStudents.stream()
                        .map(user -> new UserInfoDto(user.getId(), user.getUsername(), user.getFirstname(), user.getMiddlename(), user.getLastname()))
                        .collect(Collectors.toList()),
                pageable,
                studentsList.size()
        );
    }

    public List<ResponseUserDto> addStudentsForCourse(Long id, AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Set<User> students = userRepo.findByIdIn(addOrRemoveStudentsDto.getIds());
        for(User student : students) {
            if(!student.getStudentCourseSet().contains(course)) {
                student.getStudentCourseSet().add(course);
                userRepo.save(student);
            }
            else {
                throw new BadDataException(String.format("User with id: %s is already student of this course", student.getId()));
            }
        }
        return students.stream()
                .map(userMapper::toResponseUserDto)
                .collect(Collectors.toList());
    }

    public List<ResponseUserDto> removeStudentsForCourse(Long id, AddOrRemoveStudentsDto addOrRemoveStudentsDto) {
        Course course = courseUtils.validateAndGetCourseForTeacher(id);
        Set<User> students = userRepo.findByIdIn(addOrRemoveStudentsDto.getIds());
        for(User student : students) {
            if(student.getStudentCourseSet().contains(course)) {
                student.getStudentCourseSet().remove(course);
                userRepo.save(student);
            }
            else {
                throw new BadDataException(String.format("User with id: %s is not student of this course", student.getId()));
            }
        }
        return students.stream()
                .map(userMapper::toResponseUserDto)
                .collect(Collectors.toList());
    }
}
