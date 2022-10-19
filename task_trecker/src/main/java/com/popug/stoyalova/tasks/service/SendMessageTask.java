package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.events.TaskChangeEvent;
import com.popug.stoyalova.tasks.events.TaskCudEvent;
import com.popug.stoyalova.tasks.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask {

    private final TaskStreamingProducer producerStream;
    private final TaskBusinessProducer producerBus;

    public void sendStream(String topic, TaskCudEvent event) {
        log.info(String.format("Produced: topic: %s value size: %s", topic,
                ObjectMapperUtils.toJson(event)));

        producerStream.sendMessage(topic, event);

    }

    public void sendBus(String topic, TaskChangeEvent event) {

        producerBus.sendMessage(topic, event);

        log.info(String.format("Produced: topic: %s value size: %s", topic,
                ObjectMapperUtils.toJson(event)));
    }
}
