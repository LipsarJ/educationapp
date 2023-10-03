package com.example.educationapp.service.management.author;

import com.example.educationapp.controlleradvice.Errors;
import com.example.educationapp.dto.request.management.author.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.request.management.author.AddOrRemoveTeachersDto;
import com.example.educationapp.dto.response.UserInfoDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.exception.extend.CourseNotFoundException;
import com.example.educationapp.mapper.UserMapper;
import com.example.educationapp.repo.CourseRepo;
import com.example.educationapp.repo.UserRepo;
import com.example.educationapp.utils.CourseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorManagementService {
    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final CourseUtils courseUtils;
    private final CourseRepo courseRepo;

    public List<UserInfoDto> getAllAuthorsForCourse(Long id) {
        Course course = courseRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course is not found"));
        Set<User> authors = course.getAuthors();
        return authors.stream()
                .map(userMapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserInfoDto> addAuthorsForCourse(Long id, AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        Set<User> authors = userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds());
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        for (User author : authors) {
            if (!author.getAuthorCourseSet().contains(course)) {
                author.getAuthorCourseSet().add(course);
                userRepo.save(author);
            } else {
                throw new BadDataException(String.format("User with id: %s is already author of this course", author.getId()), Errors.AUTHOR_ALREADY_EXISTS);
            }
        }
        return authors.stream()
                .map(userMapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserInfoDto> removeAuthorsForCourse(Long id, AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        Set<User> authors = userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds());
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        for (User author : authors) {
            if (author.getAuthorCourseSet().contains(course)) {
                author.getAuthorCourseSet().remove(course);
                userRepo.save(author);
            } else {
                throw new ForbiddenException(String.format("User with id: %s is not author of this course", author.getId()));
            }
        }
        return authors.stream()
                .map(userMapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserInfoDto> getAllTeachersForCourse(Long id) {
        Course course = courseRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course is not found"));
        Set<User> teachers = course.getTeachers();
        return teachers.stream()
                .map(userMapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserInfoDto> addTeachersForCourse(Long id, AddOrRemoveTeachersDto addOrRemoveTeachersDto) {
        Set<User> teachers = userRepo.findByIdIn(addOrRemoveTeachersDto.getIds());
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        for (User teacher : teachers) {
            if (!teacher.getTeacherCourseSet().contains(course)) {
                teacher.getTeacherCourseSet().add(course);
                userRepo.save(teacher);
            } else {
                throw new BadDataException(String.format("User with id: %s is already teacher of this course", teacher.getId()), Errors.TEACHER_ALREADY_EXISTS);
            }
        }
        return teachers.stream()
                .map(userMapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<UserInfoDto> removeTeachersForCourse(Long id, AddOrRemoveTeachersDto addOrRemoveTeachersDto) {
        Set<User> teachers = userRepo.findByIdIn(addOrRemoveTeachersDto.getIds());
        Course course = courseUtils.validateAndGetCourseForAuthor(id);
        for (User teacher : teachers) {
            if (teacher.getTeacherCourseSet().contains(course)) {
                teacher.getTeacherCourseSet().remove(course);
                userRepo.save(teacher);
            } else {
                throw new ForbiddenException(String.format("User with id: %s is not teacher of this course", teacher.getId()));
            }
        }
        return teachers.stream()
                .map(userMapper::toUserInfoDto)
                .collect(Collectors.toList());
    }
}
