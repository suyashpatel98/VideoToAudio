package com.generic.controller;

import com.generic.service.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private KafkaConsumerService kafkaConsumerService;

    @Autowired
    public ConsumerController(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("/test")
    public void test() {
        kafkaConsumerService.startConsuming();
    }
}
