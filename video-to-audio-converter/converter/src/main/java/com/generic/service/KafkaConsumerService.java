package com.generic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.config.Message;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private KafkaConsumer<String, String> consumer;

    private String topic;

    private S3Service s3Service;

    public KafkaConsumerService(
            @Value("${kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${kafka.topic}") String topic,
            @Value("${kafka.consumer.group-id}") String consumerGroupId,
            S3Service s3Service) {
        this.topic = topic;
        // Initialize Kafka Consumer properties
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        this.consumer = new KafkaConsumer<>(consumerProps);
        this.s3Service = s3Service;
    }

    // Manually consume messages
    @PostConstruct
    public void startConsuming() {
        consumer.subscribe(List.of(topic));

        new Thread(() -> {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // adjust polling duration as needed
                records.forEach(record -> processMessage(record));
            }
        }).start();
    }

    private void processMessage(ConsumerRecord<String, String> record) {
        String message = record.value();
        logger.info("Consumed message: {}", message);
        Message event = null;
        try {
            event = objectMapper.readValue(message, Message.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse message: {}. Exception: {}", message, e);
        }
        s3Service.downloadFileAndProcess(event);
    }

    public void close() {
        consumer.close();
    }
}
