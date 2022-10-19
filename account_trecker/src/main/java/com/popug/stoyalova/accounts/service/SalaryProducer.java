package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.events.SalaryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryProducer {

    private final KafkaTemplate<String, SalaryEvent> kafkaTemplate;

    public void sendMessage(String topic, SalaryEvent message) {

        kafkaTemplate.send(topic, message);
    }
}
