package com.example.educationapp.service.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.NotFoundException;
import com.example.educationapp.repo.*;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class FileService {
    @Value("${minio.client.endpoint}")
    private String minioEndpoint;
    @Value("${minio.client.accessKey}")
    private String minioAccessKey;
    @Value("${minio.client.secretKey}")
    private String minioSecretKey;
    private final LessonRepo lessonRepo;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final HomeworkDoneRepo homeworkDoneRepo;
    private final MediaLessonRepo mediaLessonRepo;
    private final MediaHomeworkTaskRepo mediaHomeworkTaskRepo;
    private final MediaHomeworkDoneRepo mediaHomeworkDoneRepo;

    public UploadFileResDto uploadFile(MultipartFile file, Long fileId, Long id, String mediaOwner) {
        String bucket = UUID.randomUUID().toString();
        String objectName = file.getOriginalFilename();
        UploadFileResDto uploadFileResDto = new UploadFileResDto();
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            switch (mediaOwner) {
                case "LESSON" -> {
                    Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new NotFoundException("Lesson is not found"));
                    MediaLesson mediaLesson = new MediaLesson();
                    mediaLesson.setMediaLesson(lesson);
                    mediaLesson.setFileKey(bucket);
                    mediaLesson.setMediaType(MediaType.DOCUMENT);
                    mediaLesson.setSize(file.getSize());
                    mediaLesson.setName(objectName);
                    mediaLessonRepo.save(mediaLesson);
                    lesson.getMediaLessonList().add(mediaLesson);
                }
                case "HOMEWORK_TASK" -> {
                    HomeworkTask homeworkTask = homeworkTaskRepo.findById(id).orElseThrow(() -> new NotFoundException("Homework Task is not found"));
                    MediaHomeworkTask mediaHomeworkTask = new MediaHomeworkTask();
                    mediaHomeworkTask.setTaskMedia(homeworkTask);
                    mediaHomeworkTask.setFileKey(bucket);
                    mediaHomeworkTask.setMediaType(MediaType.DOCUMENT);
                    mediaHomeworkTask.setSize(file.getSize());
                    mediaHomeworkTask.setName(objectName);
                    mediaHomeworkTaskRepo.save(mediaHomeworkTask);
                    homeworkTask.getMediaHomeworkTaskList().add(mediaHomeworkTask);
                }
                case "HOMEWORK_DONE" -> {
                    HomeworkDone homeworkDone = homeworkDoneRepo.findById(id).orElseThrow(() -> new NotFoundException("HomeworkDone is not found"));
                    MediaHomeworkDone mediaHomeworkDone = new MediaHomeworkDone();
                    mediaHomeworkDone.setHomeworkDone(homeworkDone);
                    mediaHomeworkDone.setFileKey(bucket);
                    mediaHomeworkDone.setMediaType(MediaType.DOCUMENT);
                    mediaHomeworkDone.setSize(file.getSize());
                    mediaHomeworkDone.setName(objectName);
                    mediaHomeworkDoneRepo.save(mediaHomeworkDone);
                    homeworkDone.getMediaHomeworkDoneList().add(mediaHomeworkDone);
                }
            }
            uploadFileResDto.setFileKey(bucket);
            uploadFileResDto.setName(objectName);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return uploadFileResDto;
    }
}
