package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.events.Event;
import com.popug.stoyalova.tasks.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask {

    private final TaskProducer producer;

    public void send(String topic, Event event) {

        producer.sendMessage(topic, ObjectMapperUtils.toJson(event));

        log.info(String.format("Produced: topic: %s value size: %s", topic,
                ObjectMapperUtils.toJson(event)));
    }
}
