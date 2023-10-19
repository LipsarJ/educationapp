package com.example.educationapp.service.files;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class MinioService {
    @Value("${minio.bucket}")
    private String bucket;
    private final MinioClient minioClient;


    public void createBucketIfNotExists() throws Exception {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public void uploadFile(String object, InputStream inputStream) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .stream(inputStream, inputStream.available(), -1)
                .build());
    }

    public InputStream downloadFile(String fileKey) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileKey).build());
    }
}