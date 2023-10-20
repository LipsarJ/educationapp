package com.example.educationapp.service.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.ForbiddenException;
import com.example.educationapp.exception.NotFoundException;
import com.example.educationapp.mapper.MediaMapper;
import com.example.educationapp.repo.*;
import com.example.educationapp.security.service.UserContext;
import com.example.educationapp.utils.HomeworkUtils;
import com.example.educationapp.utils.LessonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final HomeworkUtils homeworkUtils;
    private final UserContext userContext;
    private final UserRepo userRepo;


    public UploadFileResDto uploadFileLesson(Long courseId, Long lessonId, MultipartFile file, Long fileId) {
        String object = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        UploadFileResDto uploadFileResDto = new UploadFileResDto();
        try (InputStream inputStream = file.getInputStream()) {
            minioService.createBucketIfNotExists();
            minioService.uploadFile(object, inputStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        Lesson lesson = lessonUtils.getLessonForAuthorValidatedCourse(courseId, lessonId);
        MediaLesson mediaLesson = new MediaLesson();
        mediaLesson.setMediaLesson(lesson);
        mediaLesson.setFileKey(object);
        mediaLesson.setMediaType(MediaType.DOCUMENT);
        mediaLesson.setSize(file.getSize());
        mediaLesson.setName(fileName);
        mediaLessonRepo.save(mediaLesson);
        lesson.getMediaLessonList().add(mediaLesson);
        uploadFileResDto.setFileKey(object.getBytes());
        uploadFileResDto.setName(fileName);

        return uploadFileResDto;
    }

    public UploadFileResDto uploadFileHomeworkTask(Long courseId, Long homeworkTaskId, Long lessonId, MultipartFile file, Long fileId) {
        String object = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        UploadFileResDto uploadFileResDto = new UploadFileResDto();
        try (InputStream inputStream = file.getInputStream()) {
            minioService.createBucketIfNotExists();
            minioService.uploadFile(object, inputStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        HomeworkTask homeworkTask = homeworkUtils.getHomeworkTaskForAuthorValidatedLesson(courseId, lessonId, homeworkTaskId);
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
        uploadFileResDto.setFileKey(object.getBytes());
        uploadFileResDto.setName(fileName);

        return uploadFileResDto;
    }

    public UploadFileResDto uploadFileHomeworkDone(Long courseId, Long lessonId, Long homeworkTaskId, MultipartFile file, Long fileId) {
        String object = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        UploadFileResDto uploadFileResDto = new UploadFileResDto();
        try (InputStream inputStream = file.getInputStream()) {
            minioService.createBucketIfNotExists();
            minioService.uploadFile(object, inputStream);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        HomeworkDone homeworkDone = homeworkUtils.getHomeworkDoneForStudent(courseId, lessonId, homeworkTaskId);
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
        uploadFileResDto.setFileKey(object.getBytes());
        uploadFileResDto.setName(fileName);

        return uploadFileResDto;
    }

    public UploadFileResDto downloadFileLesson(Long courseId, UUID id) throws IOException {
        lessonUtils.validateAllUsersForCourse(courseId);
        MediaLesson mediaLesson = mediaLessonRepo.findById(id).orElseThrow(() -> new NotFoundException("File is not found"));
        String fileKey = mediaLesson.getFileKey();
        String originalFileName = mediaLesson.getName();
        return downloadAndPrepareResponse(fileKey, originalFileName);
    }

    public UploadFileResDto downloadFileHomeworkTask(Long courseId, UUID id) throws IOException {
        lessonUtils.validateAllUsersForCourse(courseId);
        MediaHomeworkTask mediaHomeworkTask = mediaHomeworkTaskRepo.findById(id).orElseThrow(() -> new NotFoundException("File is not found"));
        String fileKey = mediaHomeworkTask.getFileKey();
        String originalFileName = mediaHomeworkTask.getName();
        return downloadAndPrepareResponse(fileKey, originalFileName);
    }

    public UploadFileResDto downloadFileHomeworkDone(Long courseId, UUID id) throws IOException {
        lessonUtils.validateAllUsersForCourse(courseId);
        MediaHomeworkDone mediaHomeworkDone = mediaHomeworkDoneRepo.findById(id).orElseThrow(() -> new NotFoundException("File is not found"));
        String fileKey = mediaHomeworkDone.getFileKey();
        String originalFileName = mediaHomeworkDone.getName();
        return downloadAndPrepareResponse(fileKey, originalFileName);
    }

    public void deleteFileLesson(Long courseId, Long lessonId, UUID id) {
        lessonUtils.getLessonForAuthorValidatedCourse(courseId, lessonId);
        MediaLesson mediaLesson = mediaLessonRepo.findById(id).orElseThrow(() -> new NotFoundException("Media is not found"));
        deleteMediaFile(mediaLesson.getFileKey());
        mediaLessonRepo.delete(mediaLesson);
    }

    public void deleteFileHomeworkTask(Long courseId, Long lessonId, Long homeworkTaskId, UUID id) {
        homeworkUtils.getHomeworkTaskForAuthorValidatedLesson(courseId, lessonId, homeworkTaskId);
        MediaHomeworkTask mediaHomeworkTask = mediaHomeworkTaskRepo.findById(id).orElseThrow(() -> new NotFoundException("Homework task is not found"));
        deleteMediaFile(mediaHomeworkTask.getFileKey());
        mediaHomeworkTaskRepo.delete(mediaHomeworkTask);
    }

    public void deleteFileHomeworkDone(Long courseId, Long lessonId, Long homeworkTaskId, UUID id) {
        homeworkUtils.getHomeworkDoneForStudent(courseId, lessonId, homeworkTaskId);
        MediaHomeworkDone mediaHomeworkDone = mediaHomeworkDoneRepo.findById(id).orElseThrow(() -> new NotFoundException("Homework done is not found"));
        deleteMediaFile(mediaHomeworkDone.getFileKey());
        mediaHomeworkDoneRepo.delete(mediaHomeworkDone);
    }

    public List<UploadFileResDto> getAllFilesLesson(Long courseId, Long lessonId) {
        lessonUtils.validateAllUsersForCourse(courseId);
        Lesson lesson = lessonRepo.findById(lessonId).orElseThrow(() -> new NotFoundException("Lesson is not found"));
        return lesson.getMediaLessonList()
                .stream()
                .map(mediaMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public List<UploadFileResDto> getAllFilesHomeworkTask(Long courseId, Long homeworkTaskId) {
        lessonUtils.validateAllUsersForCourse(courseId);
        HomeworkTask homeworkTask = homeworkTaskRepo.findById(homeworkTaskId).orElseThrow(() -> new NotFoundException("Homework Task is not found"));
        return homeworkTask.getMediaHomeworkTaskList()
                .stream()
                .map(mediaMapper::toHomeworkTaskDto)
                .collect(Collectors.toList());
    }

    public List<UploadFileResDto> getAllFilesHomeworkDone(Long courseId, Long homeworkDoneId) {
        lessonUtils.validateAllUsersForCourse(courseId);
        HomeworkDone homeworkDone = homeworkDoneRepo.findById(homeworkDoneId).orElseThrow(() -> new NotFoundException("HomeworkDone is not found"));
        return homeworkDone.getMediaHomeworkDoneList()
                .stream()
                .map(mediaMapper::toHomeworkDoneDto)
                .collect(Collectors.toList());
    }

    private UploadFileResDto downloadAndPrepareResponse(String fileKey, String originalFileName) throws IOException {
        try (InputStream response = minioService.downloadFile(fileKey)) {
            return new UploadFileResDto(
                    URLEncoder.encode(originalFileName, StandardCharsets.UTF_8),
                    response.readAllBytes()
            );
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private void deleteMediaFile(String fileKey) {
        try {
            minioService.deleteFile(fileKey);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
