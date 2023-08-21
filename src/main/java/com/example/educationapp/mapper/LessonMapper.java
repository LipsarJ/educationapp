package com.example.educationapp.mapper;

import com.example.educationapp.dto.LessonDto;
import com.example.educationapp.entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface LessonMapper {
    Lesson toEntity(LessonDto lessonDto);

    LessonDto toDto(Lesson lesson);
}
