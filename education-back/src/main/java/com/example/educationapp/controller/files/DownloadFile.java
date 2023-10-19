package com.example.educationapp.controller.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.service.files.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/download/{courseId}/{mediaOwner}/{id}")
public class DownloadFile {
    private final FileService fileService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long courseId, @PathVariable UUID id, @PathVariable String mediaOwner) {
        try {
            UploadFileResDto uploadFileResDto = fileService.downloadFile(courseId, id, mediaOwner);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uploadFileResDto.getName())
                    .body(uploadFileResDto.getFileKey());
        } catch (IOException e) {
            return ResponseEntity.status(404).body(e.getMessage().getBytes());
        }
    }
}
