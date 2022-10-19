package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.events.TaskCudEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskStreamingProducer {

    private final KafkaTemplate<String, TaskCudEvent> kafkaTemplate;

    @SneakyThrows
    public void sendMessage(String topic, TaskCudEvent message) {
        kafkaTemplate.send(topic, message);
    }
}
