package com.example.educationapp.controller.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.service.files.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/upload/{courseId}/{mediaOwner}/{id}")
public class UploadFile {
    private final FileService fileService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UploadFileResDto uploadFile(@PathVariable Long courseId,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("fileId") Long fileId, @PathVariable Long id, @PathVariable String mediaOwner) {
        return fileService.uploadFile(courseId, file, fileId, id, mediaOwner);
    }
}
