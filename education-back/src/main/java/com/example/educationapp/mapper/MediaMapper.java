package com.example.educationapp.mapper;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.entity.MediaHomeworkDone;
import com.example.educationapp.entity.MediaHomeworkTask;
import com.example.educationapp.entity.MediaLesson;
import org.mapstruct.Mapper;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    default byte[] mapStringToByteArray(String value) {
        return value.getBytes(StandardCharsets.UTF_8);
    }

    UploadFileResDto toLessonDto(MediaLesson mediaLesson);

    UploadFileResDto toHomeworkTaskDto(MediaHomeworkTask mediaHomeworkTask);

    UploadFileResDto toHomeworkDoneDto(MediaHomeworkDone mediaHomeworkDone);
}
