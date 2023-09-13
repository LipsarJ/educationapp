package com.example.educationapp.mapper.student;

import com.example.educationapp.dto.request.student.RequestHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseCourseStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkDoneStudentDto;
import com.example.educationapp.dto.response.student.ResponseHomeworkTaskStudentDto;
import com.example.educationapp.dto.response.student.ResponseLessonStudentDto;
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

    @Mapping(target = "lessons", source = "course.lessonList")
    ResponseCourseStudentDto toResponseCourseDto(Course course);

    @Mapping(target = "homeworkTasks", source = "lesson.homeworkTaskList")
        //@Mapping(target = "mediaLessons", source = "lesson.mediaLessonList")
    ResponseLessonStudentDto toResponseLessonDto(Lesson lesson);

    //@Mapping(target = "mediaHomeworkTasks", source = "homeworkTask.mediaHomeworkTaskList")
    ResponseHomeworkTaskStudentDto toResponseHomeworkTaskDto(HomeworkTask homeworkTask);

    @Mapping(target = "teacherInfoDto", source = "homeworkDone.teacher")
    //@Mapping(target = "mediaHomeworkDones", source = "homeworkDone.mediaHomeworkDoneList")
    ResponseHomeworkDoneStudentDto toResponseHomeworkDoneDto(HomeworkDone homeworkDone);

    //@Mapping(target = "mediaHomeworkDoneList", source = "requestHomeworkDoneStudentDto.mediaHomeworkDones")
    HomeworkDone toHomeworkDone(RequestHomeworkDoneStudentDto requestHomeworkDoneStudentDto);
}
