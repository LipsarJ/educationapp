package com.example.educationapp.mapper;

import com.example.educationapp.dto.request.RequestLessonDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface LessonMapper {
    Lesson toEntity(RequestLessonDto requestLessonDto);

    ResponseLessonDto toResponseDto(Lesson lesson);
}
