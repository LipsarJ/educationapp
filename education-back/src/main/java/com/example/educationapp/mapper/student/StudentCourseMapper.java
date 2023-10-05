package com.example.educationapp.mapper.student;

import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.ResponseCourseDto;
import com.example.educationapp.dto.response.ResponseHomeworkTaskDto;
import com.example.educationapp.dto.response.ResponseLessonDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.entity.Course;
import com.example.educationapp.entity.HomeworkDone;
import com.example.educationapp.entity.HomeworkTask;
import com.example.educationapp.entity.Lesson;
import com.example.educationapp.mapper.BaseLocalDateTimeOffsetDateTimeMapper;
import com.example.educationapp.mapper.HomeworkTaskMapper;
import com.example.educationapp.mapper.LessonMapper;
import com.example.educationapp.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BaseLocalDateTimeOffsetDateTimeMapper.class, LessonMapper.class, HomeworkTaskMapper.class, UserMapper.class})
public interface StudentCourseMapper {

    ResponseCourseDto toResponseCourseDto(Course course);

    //@Mapping(target = "mediaLessons", source = "lesson.mediaLessonList")
    ResponseLessonDto toResponseLessonDto(Lesson lesson);

    //@Mapping(target = "mediaHomeworkTasks", source = "homeworkTask.mediaHomeworkTaskList")
    ResponseHomeworkTaskDto toResponseHomeworkTaskDto(HomeworkTask homeworkTask);

    @Mapping(target = "teacherInfoDto", source = "homeworkDone.teacher")
    @Mapping(target = "studentInfoDto", source = "homeworkDone.student")
        //@Mapping(target = "mediaHomeworkDones", source = "homeworkDone.mediaHomeworkDoneList")
    ResponseHomeworkDoneStudentDto toResponseHomeworkDoneDto(HomeworkDone homeworkDone);

    //@Mapping(target = "mediaHomeworkDoneList", source = "requestHomeworkDoneStudentDto.mediaHomeworkDones")
    HomeworkDone toHomeworkDone(RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto);
}
