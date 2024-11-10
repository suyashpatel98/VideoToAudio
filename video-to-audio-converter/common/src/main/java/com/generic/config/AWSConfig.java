package com.generic.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;

@Configuration
public class AWSConfig {

    private static final Logger logger = LoggerFactory.getLogger(AWSConfig.class);

    @Bean
    public StaticCredentialsProvider awsCredentialsProvider() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("AWS credentials not found in environment variables");
        }

        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }
}
