package com.example.educationapp.mapper;

import com.example.educationapp.dto.request.RequestCourseDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface CourseMapper {
    Course toEntity(RequestCourseDto requestCourseDto);

    ResponseCourseDto toResponseDto(Course course);
}