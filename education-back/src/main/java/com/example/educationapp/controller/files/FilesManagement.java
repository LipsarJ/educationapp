package com.example.educationapp.controller.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.service.files.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FilesManagement {
    private final FileService fileService;

    @PreAuthorize("hasAnyAuthority('AUTHOR','TEACHER','STUDENT')")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE}, value = "/download/course/{courseId}/lesson/{id}")
    public ResponseEntity<byte[]> downloadFileLesson(@PathVariable Long courseId, @PathVariable UUID id) {
        try {
            UploadFileResDto uploadFileResDto = fileService.downloadFileLesson(courseId, id);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uploadFileResDto.getName())
                    .body(uploadFileResDto.getFileKey());
        } catch (IOException e) {
            return ResponseEntity.status(404).body(e.getMessage().getBytes());
        }
    }

    @PreAuthorize("hasAnyAuthority('AUTHOR','TEACHER','STUDENT')")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE}, value = "/download/course/{courseId}/lesson/homework-task/{id}")
    public ResponseEntity<byte[]> downloadFileHomeworkTask(@PathVariable Long courseId, @PathVariable UUID id) {
        try {
            UploadFileResDto uploadFileResDto = fileService.downloadFileHomeworkTask(courseId, id);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uploadFileResDto.getName())
                    .body(uploadFileResDto.getFileKey());
        } catch (IOException e) {
            return ResponseEntity.status(404).body(e.getMessage().getBytes());
        }
    }

    @PreAuthorize("hasAnyAuthority('TEACHER','STUDENT')")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE}, value = "/download/course/{courseId}/lesson/homework-task/homework-done/{id}")
    public ResponseEntity<byte[]> downloadFileHomeworkDone(@PathVariable Long courseId, @PathVariable UUID id) {
        try {
            UploadFileResDto uploadFileResDto = fileService.downloadFileHomeworkDone(courseId, id);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uploadFileResDto.getName())
                    .body(uploadFileResDto.getFileKey());
        } catch (IOException e) {
            return ResponseEntity.status(404).body(e.getMessage().getBytes());
        }
    }

    @PreAuthorize("hasAnyAuthority('AUTHOR','TEACHER','STUDENT')")
    @GetMapping("/get-all-media/course/{courseId}/lesson/{lessonId}")
    public List<UploadFileResDto> getAllFilesLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        return fileService.getAllFilesLesson(courseId, lessonId);
    }

    @PreAuthorize("hasAnyAuthority('AUTHOR','TEACHER','STUDENT')")
    @GetMapping("/get-all-media/course/{courseId}/lesson/homework-task/{homeworkTaskId}")
    public List<UploadFileResDto> getAllFilesHomeworkTask(@PathVariable Long courseId, @PathVariable Long homeworkTaskId) {
        return fileService.getAllFilesHomeworkTask(courseId, homeworkTaskId);
    }

    @PreAuthorize("hasAnyAuthority('TEACHER','STUDENT')")
    @GetMapping("/get-all-media/course/{courseId}/lesson/homework-task/homework-done/{homeworkDoneId}")
    public List<UploadFileResDto> getAllFilesHomeworkDone(@PathVariable Long courseId, @PathVariable Long homeworkDoneId) {
        return fileService.getAllFilesHomeworkDone(courseId, homeworkDoneId);
    }

    @PreAuthorize("hasAuthority('AUTHOR')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/upload/course/{courseId}/lesson/{lessonId}")
    public UploadFileResDto uploadFileLesson(@PathVariable Long courseId,
                                             @RequestParam("file") MultipartFile file,
                                             @RequestParam("fileId") Long fileId, @PathVariable Long lessonId) {
        return fileService.uploadFileLesson(courseId, lessonId, file, fileId);
    }

    @PreAuthorize("hasAuthority('AUTHOR')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/upload/course/{courseId}/lesson/{lessonId}/homework-task/{homeworkTaskId}")
    public UploadFileResDto uploadFileHomeworkTask(@PathVariable Long courseId, @PathVariable Long lessonId,
                                                   @RequestParam("file") MultipartFile file,
                                                   @RequestParam("fileId") Long fileId, @PathVariable Long homeworkTaskId) {
        return fileService.uploadFileHomeworkTask(courseId, lessonId, homeworkTaskId, file, fileId);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, value = "/upload/course/{courseId}/lesson/{lessonId}/homework-task/{homeworkTaskId}/homework-done")
    public UploadFileResDto uploadFileHomeworkDone(@PathVariable Long courseId,
                                                   @PathVariable Long lessonId,
                                                   @PathVariable Long homeworkTaskId,
                                                   @RequestParam("file") MultipartFile file,
                                                   @RequestParam("fileId") Long fileId) {
        return fileService.uploadFileHomeworkDone(courseId, lessonId, homeworkTaskId, file, fileId);
    }

    @PreAuthorize("hasAuthority('AUTHOR')")
    @DeleteMapping("/delete/course/{courseId}/lesson/{lessonId}")
    public void deleteFileLesson(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam UUID mediaId) {
        fileService.deleteFileLesson(courseId, lessonId, mediaId);
    }

    @PreAuthorize("hasAuthority('AUTHOR')")
    @DeleteMapping("/delete/course/{courseId}/lesson/{lessonId}/homework-task/{homeworkTaskId}")
    public void deleteFileHomeworkTask(@PathVariable Long courseId, @PathVariable Long lessonId, @PathVariable Long homeworkTaskId, @RequestParam UUID mediaId) {
        fileService.deleteFileHomeworkTask(courseId, lessonId, homeworkTaskId, mediaId);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/delete/course/{courseId}/lesson/{lessonId}/homework-task/{homeworkTaskId}/homework-done")
    public void deleteFileHomeworkDone(@PathVariable Long courseId, @PathVariable Long lessonId, @PathVariable Long homeworkTaskId, @RequestParam UUID mediaId) {
        fileService.deleteFileHomeworkDone(courseId, lessonId, homeworkTaskId, mediaId);
    }
}
