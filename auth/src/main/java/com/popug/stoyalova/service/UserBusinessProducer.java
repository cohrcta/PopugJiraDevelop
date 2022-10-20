package com.popug.stoyalova.service;

import com.popug.stoyalova.event.UserChangeRoleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBusinessProducer {

    private final KafkaTemplate<String, UserChangeRoleEvent> kafkaTemplate;

    public void sendMessage(String topic, UserChangeRoleEvent message) {

        kafkaTemplate.send(topic, message);
    }
}
