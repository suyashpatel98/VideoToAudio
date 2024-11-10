package com.generic.controller;

import com.generic.proxy.AuthClient;
import com.generic.service.KafkaProducerService;
import com.generic.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class UploadController {
    AuthClient authClient;
    S3Service s3Service;
    KafkaProducerService kafkaProducerService;

    @Autowired
    public UploadController(AuthClient authClient, S3Service s3Service, KafkaProducerService kafkaProducerService) {
        this.authClient = authClient;
        this.s3Service = s3Service;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/test")
    public void test() {
        boolean result = kafkaProducerService.publishMessage("email", "videoKey", "requestId");
        System.out.println(result);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestHeader(name = "Authorization") String tokenHeader,
                                         @RequestParam(name = "email") String email,
                                         @RequestPart(name = "file") MultipartFile videoFile) {
//        if(!authClient.validateToken(tokenHeader).getStatusCode().isSameCodeAs(HttpStatus.OK)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid token");
//        }
        String requestId = UUID.randomUUID().toString();
        String videoKey = null;
        try {
            videoKey = s3Service.uploadVideo(email, videoFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kafkaProducerService.publishMessage(email, videoKey, requestId);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestId);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.status(HttpStatus.OK).body("success!");
    }
}
