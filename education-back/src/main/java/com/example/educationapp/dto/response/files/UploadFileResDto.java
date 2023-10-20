package com.example.educationapp.dto.response.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResDto {
    private String name;
    private byte[] fileKey;
}
