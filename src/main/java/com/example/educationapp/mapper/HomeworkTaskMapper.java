package com.example.educationapp.mapper;

import com.example.educationapp.dto.request.RequestHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BaseLocalDateTimeOffsetDateTimeMapper.class)
public interface HomeworkTaskMapper {
    HomeworkTask toEntity(RequestHomeworkTaskDto requestHomeworkTaskDto);

    ResponseHomeworkTaskDto toResponseDto(HomeworkTask homeworkTask);
}
