package com.generic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.config.Message;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaProducerService {
    private KafkaProducer<String, String> producer;
    private String topic;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducerService(
            @Value("${kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${kafka.topic}") String topic,
            @Value("${kafka.producer.key-serializer}") String keySerializer,
            @Value("${kafka.producer.value-serializer}") String valueSerializer,
            @Value("${kafka.producer.acks}") String acks) {

        // Initialize properties for the Kafka producer
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);
        props.put("acks", acks);

        // Initialize producer and topic
        this.topic = topic;
        this.producer = new KafkaProducer<>(props);
    }

    public boolean publishMessage(String email, String videoKey, String requestId) {
        try {
            Message message = new Message(email, videoKey, requestId);
            String messageJson = objectMapper.writeValueAsString(message);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, email, messageJson);

            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        exception.printStackTrace();
                    } else {
                        System.out.println("Message sent successfully with metadata: " + metadata.toString());
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        producer.close();
    }
}
