package com.example.educationapp.controller.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.service.files.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public class DownloadFile {
    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/v1/download/{mediaOwner}/{id}")
    public class UploadFile {
        private final FileService fileService;

        @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
        public UploadFileResDto uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("fileId") Long fileId, @PathVariable Long id, @PathVariable String mediaOwner) {
            return fileService.uploadFile(file, fileId, id, mediaOwner);
        }
    }
}
