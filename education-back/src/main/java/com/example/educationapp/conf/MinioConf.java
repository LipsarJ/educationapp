package com.example.educationapp.conf;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MinioConf {
    @Value("${minio.client.endpoint}")
    private String minioEndpoint;
    @Value("${minio.client.accessKey}")
    private String minioAccessKey;
    @Value("${minio.client.secretKey}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }
}