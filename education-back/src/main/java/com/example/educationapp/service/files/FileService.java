package com.example.educationapp.service.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.exception.NotFoundException;
import com.example.educationapp.mapper.MediaMapper;
import com.example.educationapp.repo.*;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.LessonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final LessonRepo lessonRepo;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final HomeworkDoneRepo homeworkDoneRepo;
    private final MediaLessonRepo mediaLessonRepo;
    private final MediaHomeworkTaskRepo mediaHomeworkTaskRepo;
    private final MediaHomeworkDoneRepo mediaHomeworkDoneRepo;
    private final MediaMapper mediaMapper;
    private final MinioService minioService;
    private final LessonUtils lessonUtils;
    private final UserContext userContext;
    private final UserRepo userRepo;


    public UploadFileResDto uploadFile(Long courseId, MultipartFile file, Long fileId, Long id, String mediaOwner) {
        String object = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        UploadFileResDto uploadFileResDto = new UploadFileResDto();
        try (InputStream inputStream = file.getInputStream()) {
            minioService.createBucketIfNotExists();
            minioService.uploadFile(object, inputStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        switch (mediaOwner) {
            case "LESSON" -> {
                Lesson lesson = lessonUtils.getLessonForAuthorValidatedCourse(courseId, id);
                MediaLesson mediaLesson = new MediaLesson();
                mediaLesson.setMediaLesson(lesson);
                mediaLesson.setFileKey(object);
                mediaLesson.setMediaType(MediaType.DOCUMENT);
                mediaLesson.setSize(file.getSize());
                mediaLesson.setName(fileName);
                mediaLessonRepo.save(mediaLesson);
                lesson.getMediaLessonList().add(mediaLesson);
            }
            case "HOMEWORK_TASK" -> {
                HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new NotFoundException("Homework Task is not found"));
                if (!homeworkTask.getLesson().getLessonsCourse().getAuthors().contains(userRepo.findById(userContext.getUserDto().getId()))) {
                    throw new ForbiddenException("You are not author of this course");
                }
                MediaHomeworkTask mediaHomeworkTask = new MediaHomeworkTask();
                mediaHomeworkTask.setTaskMedia(homeworkTask);
                mediaHomeworkTask.setFileKey(object);
                mediaHomeworkTask.setMediaType(MediaType.DOCUMENT);
                mediaHomeworkTask.setSize(file.getSize());
                mediaHomeworkTask.setName(fileName);
                mediaHomeworkTaskRepo.save(mediaHomeworkTask);
                homeworkTask.getMediaHomeworkTaskList().add(mediaHomeworkTask);
            }
            case "HOMEWORK_DONE" -> {
                HomeworkDone homeworkDone = homeworkDoneRepo.findById(id).orElseThrow(() -> new NotFoundException("HomeworkDone is not found"));
                if (!homeworkDone.getTask().getLesson().getLessonsCourse().getStudents().contains(homeworkDone.getStudent())) {
                    throw new ForbiddenException("You are not student of this course");
                }
                MediaHomeworkDone mediaHomeworkDone = new MediaHomeworkDone();
                mediaHomeworkDone.setHomeworkDone(homeworkDone);
                mediaHomeworkDone.setFileKey(object);
                mediaHomeworkDone.setMediaType(MediaType.DOCUMENT);
                mediaHomeworkDone.setSize(file.getSize());
                mediaHomeworkDone.setName(fileName);
                mediaHomeworkDoneRepo.save(mediaHomeworkDone);
                homeworkDone.getMediaHomeworkDoneList().add(mediaHomeworkDone);
            }
        }
        uploadFileResDto.setFileKey(object.getBytes());
        uploadFileResDto.setName(fileName);

        return uploadFileResDto;
    }

    public UploadFileResDto downloadFile(Long courseId, UUID id, String mediaOwner) throws IOException {
        String fileKey = "";
        String originalFileName = "";
        lessonUtils.validateAllUsersForCourse(courseId);
        switch (mediaOwner) {
            case "LESSON" -> {
                MediaLesson mediaLesson = mediaLessonRepo.findById(id).orElseThrow(() -> new NotFoundException("File is not found"));
                fileKey = mediaLesson.getFileKey();
                originalFileName = mediaLesson.getName();
            }
            case "HOMEWORK_TASK" -> {
                MediaHomeworkTask mediaHomeworkTask = mediaHomeworkTaskRepo.findById(id).orElseThrow(() -> new NotFoundException("File is not found"));
                fileKey = mediaHomeworkTask.getFileKey();
                originalFileName = mediaHomeworkTask.getName();
            }
            case "HOMEWORK_DONE" -> {
                MediaHomeworkDone mediaHomeworkDone = mediaHomeworkDoneRepo.findById(id).orElseThrow(() -> new NotFoundException("File is not found"));
                fileKey = mediaHomeworkDone.getFileKey();
                originalFileName = mediaHomeworkDone.getName();
            }
        }
        try (InputStream response = minioService.downloadFile(fileKey)) {
            return new UploadFileResDto(
                    URLEncoder.encode(originalFileName, StandardCharsets.UTF_8),
                    response.readAllBytes()
            );
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void deleteFile(Long courseId, Long ownerId, UUID id, String mediaOwner) {
        String fileKey = "";
        switch (mediaOwner) {
            case "LESSON" -> {
                lessonUtils.getLessonForAuthorValidatedCourse(courseId, ownerId);
                MediaLesson mediaLesson = mediaLessonRepo.findById(id).orElseThrow(() -> new NotFoundException("Media is not found"));
                fileKey = mediaLesson.getFileKey();
                mediaLessonRepo.delete(mediaLesson);
            }
            case "HOMEWORK_TASK" -> {
                HomeworkTask homeworkTask = homeworkTaskRepo.findById(ownerId).orElseThrow(() -> new NotFoundException("Homework Task is not found"));
                if (!homeworkTask.getLesson().getLessonsCourse().getAuthors().contains(userRepo.findById(userContext.getUserDto().getId()))) {
                    throw new ForbiddenException("You are not author of this course");
                }
                MediaHomeworkTask mediaHomeworkTask = mediaHomeworkTaskRepo.findById(id).orElseThrow(() -> new NotFoundException("Homework task is not found"));
                fileKey = mediaHomeworkTask.getFileKey();
                mediaHomeworkTaskRepo.delete(mediaHomeworkTask);
            }
            case "HOMEWORK_DONE" -> {
                HomeworkDone homeworkDone = homeworkDoneRepo.findById(ownerId).orElseThrow(() -> new NotFoundException("HomeworkDone is not found"));
                if (!homeworkDone.getTask().getLesson().getLessonsCourse().getStudents().contains(homeworkDone.getStudent())) {
                    throw new ForbiddenException("You are not student of this course");
                }
                MediaHomeworkDone mediaHomeworkDone = mediaHomeworkDoneRepo.findById(id).orElseThrow(() -> new NotFoundException("Homework done is not found"));
                fileKey = mediaHomeworkDone.getFileKey();
                mediaHomeworkDoneRepo.delete(mediaHomeworkDone);
            }
        }
        try {
            minioService.deleteFile(fileKey);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<UploadFileResDto> getAllFiles(Long courseId, Long id, String mediaOwner) {
        List<UploadFileResDto> files = new ArrayList<>();
        lessonUtils.validateAllUsersForCourse(courseId);
        switch (mediaOwner) {
            case "LESSON" -> {
                files.addAll(lessonRepo.findById(id).
                        orElseThrow(() -> new NotFoundException("Lesson is not found"))
                        .getMediaLessonList().stream().map(mediaMapper::toLessonDto).toList());
            }
            case "HOMEWORK_TASK" -> {
                files.addAll(homeworkTaskRepo.findById(id).
                        orElseThrow(() -> new NotFoundException("Lesson is not found"))
                        .getMediaHomeworkTaskList().stream().map(mediaMapper::toHomeworkTaskDto).toList());
            }
            case "HOMEWORK_DONE" -> {
                files.addAll(homeworkDoneRepo.findById(id).
                        orElseThrow(() -> new NotFoundException("Lesson is not found"))
                        .getMediaHomeworkDoneList().stream().map(mediaMapper::toHomeworkDoneDto).toList());
            }
        }
        return files;
    }
}
