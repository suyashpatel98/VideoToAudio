package com.generic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

@Service
public class S3Service {
    final static String BUCKET_NAME = "videobucketsuyash";

    private final S3Client s3Client;

    @Autowired
    public S3Service(StaticCredentialsProvider credentialsProvider) {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(credentialsProvider)
                .build();

    }
    public String uploadVideo(String email, MultipartFile videoFile) throws IOException {
        String key = videoFile.getOriginalFilename();
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME).key(key).build();

        s3Client.putObject(request, RequestBody.fromBytes(videoFile.getBytes()));
        /**
         * // Stream the file directly to S3
         *     try (InputStream videoStream = videoFile.getInputStream()) {
         *         s3Client.putObject(request, RequestBody.fromInputStream(videoStream, videoFile.getSize()));
         *     }
         */
        return key;
    }
}
