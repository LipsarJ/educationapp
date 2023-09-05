package com.example.educationapp.service.authormanagement;

import com.example.educationapp.dto.request.authormanagement.AddOrRemoveAuthorsDto;
import com.example.educationapp.dto.response.ResponseUserDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.User;
import com.example.educationapp.exception.BadDataException;
import com.example.educationapp.mapper.UserMapper;
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

    public List<ResponseUserDto> getAllAuthorsForCourse(Long id) {
        Course course = courseUtils.validateAndGetCourse(id);
        Set<User> authors = course.getAuthors();
        return authors.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ResponseUserDto> addAuthorsForCourse(Long id, AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        Set<User> authors = userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds());
        Course course = courseUtils.validateAndGetCourse(id);
        for (User author : authors) {
            if (!author.getAuthorCourseSet().contains(course)) {
                author.getAuthorCourseSet().add(course);
                userRepo.save(author);
            } else {
                throw new BadDataException(String.format("User with id: %s is already author of this course", author.getId()));
            }
        }
        return authors.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ResponseUserDto> removeAuthorsForCourse(Long id, AddOrRemoveAuthorsDto addOrRemoveAuthorsDto) {
        Set<User> authors = userRepo.findByIdIn(addOrRemoveAuthorsDto.getIds());
        Course course = courseUtils.validateAndGetCourse(id);
        for (User author : authors) {
            if (author.getAuthorCourseSet().contains(course)) {
                author.getAuthorCourseSet().remove(course);
                userRepo.save(author);
            } else {
                throw new BadDataException(String.format("User with id: %s is not author of this course", author.getId()));
            }
        }
        return authors.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
