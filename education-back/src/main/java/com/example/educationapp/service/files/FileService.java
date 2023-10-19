package com.example.educationapp.service.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.entity.*;
import com.example.educationapp.exception.NotFoundException;
import com.example.educationapp.repo.*;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class FileService {
    @Value("${minio.bucket}")
    private String bucket;
    private final LessonRepo lessonRepo;
    private final HomeworkTaskRepo homeworkTaskRepo;
    private final HomeworkDoneRepo homeworkDoneRepo;
    private final MediaLessonRepo mediaLessonRepo;
    private final MediaHomeworkTaskRepo mediaHomeworkTaskRepo;
    private final MediaHomeworkDoneRepo mediaHomeworkDoneRepo;
    private final MinioClient minioClient;

    public UploadFileResDto uploadFile(MultipartFile file, Long fileId, Long id, String mediaOwner) {
        String object = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        UploadFileResDto uploadFileResDto = new UploadFileResDto();
        try (InputStream inputStream = file.getInputStream()) {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            switch (mediaOwner) {
                case "LESSON" -> {
                    Lesson lesson = lessonRepo.findById(id).orElseThrow(() -> new NotFoundException("Lesson is not found"));
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
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return uploadFileResDto;
    }

    public UploadFileResDto downloadFile(UUID id, String mediaOwner) throws IOException {
        String fileKey = "";
        String originalFileName = "";
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
        try (InputStream response = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileKey).build())) {
            return new UploadFileResDto(
                    URLEncoder.encode(originalFileName, StandardCharsets.UTF_8),
                    response.readAllBytes()
            );
        } catch (InvalidResponseException | XmlParserException | ServerException | NoSuchAlgorithmException |
                 InsufficientDataException | InvalidKeyException | ErrorResponseException | InternalException e) {
            throw new IOException(e);
        }
    }
}
