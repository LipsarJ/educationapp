package com.example.educationapp.controller.files;

import com.example.educationapp.service.files.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delete/{courseId}/{mediaOwner}/{id}")
public class DeleteFile {
    private final FileService fileService;

    @DeleteMapping
    public void deleteFile(@PathVariable Long courseId, @PathVariable String mediaOwner, @PathVariable Long id, @RequestParam UUID mediaId) {
        fileService.deleteFile(courseId, id, mediaId, mediaOwner);
    }
}
