package com.example.educationapp.controller.files;

import com.example.educationapp.dto.response.files.UploadFileResDto;
import com.example.educationapp.service.files.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{mediaOwner}/{id}/get-all-media")
public class GetFiles {
    private final FileService fileService;
    @GetMapping
    public List<UploadFileResDto> getAllFiles(@PathVariable Long id, @PathVariable String mediaOwner) {
        return fileService.getAllFiles(id, mediaOwner);
    }
}
