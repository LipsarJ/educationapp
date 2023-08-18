package com.example.educationapp.mapper;

import com.example.educationapp.dto.CourseDto;
import com.example.educationapp.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CourseDto courseDto);

    CourseDto toDto(Course course);
}