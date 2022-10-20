package com.popug.stoyalova.service;

import com.popug.stoyalova.event.UserCudEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStreamingProducer {

    private final KafkaTemplate<String, UserCudEvent> kafkaTemplate;

    public void sendMessage(String topic, UserCudEvent message) {

        kafkaTemplate.send(topic, message);
    }
}
