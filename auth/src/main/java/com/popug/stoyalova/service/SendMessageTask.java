package com.popug.stoyalova.service;

import com.popug.stoyalova.event.UserEvent;
import com.popug.stoyalova.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask {

    private final UserProducer producer;

    public void send(String topic, UserEvent event) {

        producer.sendMessage(topic, ObjectMapperUtils.toJson(event));

        log.info(String.format("Produced: topic: %s value size: %s", topic,
                ObjectMapperUtils.toJson(event)));
    }
}
