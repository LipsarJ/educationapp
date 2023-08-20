package com.example.educationapp.mapper;

import com.example.educationapp.dto.HomeworkTaskDto;
import com.example.educationapp.entity.HomeworkTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HomeworkTaskMapper {
    HomeworkTask toEntity(HomeworkTaskDto homeworkTaskDto);

    HomeworkTaskDto toDto(HomeworkTask homeworkTask);
}
