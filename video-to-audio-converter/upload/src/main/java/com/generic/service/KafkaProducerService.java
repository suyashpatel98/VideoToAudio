package com.generic.service;

import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaProducerService {
    private KafkaProducer<String, String> producer;
    private String topic;

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
            String message = String.format("{\"email\": \"%s\", \"videoKey\": \"%s\", \"requestId\": \"%s\"}", email, videoKey, requestId);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, email, message);

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
