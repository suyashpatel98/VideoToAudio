package com.generic.service;

import com.generic.config.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private final static String BUCKET_NAME = "videobucketsuyash";

    private static final String TEMP_DIR = "/Users/suyash/Downloads/delete";

    private final S3Client s3Client;

    @Autowired
    public S3Service(StaticCredentialsProvider credentialsProvider) {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(credentialsProvider)
                .build();

    }

    public void downloadFileAndProcess(Message message) {
        String videoKey = message.getVideoKey();
        String email = message.getEmail();
        String requestId = message.getRequestId();

        File videoFile = null;
        File audioFile = null;

        try {
            videoFile = downloadFromS3(videoKey);
            logger.info("videoFile downloaded: {}", videoFile.getName());
            audioFile = convertVideoToAudio(videoFile);

            String audioKey = uploadAudioToS3(audioFile, requestId);
        } catch (IOException e) {
            System.err.println("Error processing video: " + e.getMessage());
        }
//        finally {
//            if (videoFile != null) videoFile.delete();
//            if (audioFile != null) audioFile.delete();
//        }
    }

    private String uploadAudioToS3(File audioFile, String requestId) {

        // TODO
        return "randomString";
    }

    private File convertVideoToAudio(File videoFile) {
        // TODO
        return null;
    }

    private File downloadFromS3(String videoKey) throws IOException {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(videoKey)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

        String fileName = Path.of(videoKey).getFileName().toString();
        File downloadedFile = new File(TEMP_DIR, fileName);
        logger.info("Parent directory exists: {}, can write: {}",
                downloadedFile.getParentFile().exists(),
                downloadedFile.getParentFile().canWrite());
        logger.info("Downloading file to: {}", downloadedFile.getAbsolutePath());
        if (!downloadedFile.getParentFile().exists()) {
            if (!downloadedFile.getParentFile().mkdirs()) {
                throw new IOException("Failed to create directory: " + downloadedFile.getParent());
            }
        }
        try (FileOutputStream fos = new FileOutputStream(downloadedFile)) {
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3Object.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            fos.flush();
        } catch (IOException e) {
            logger.error("Error writing file: " + e.getMessage());
            throw e;
        }
        return downloadedFile;
    }
}
