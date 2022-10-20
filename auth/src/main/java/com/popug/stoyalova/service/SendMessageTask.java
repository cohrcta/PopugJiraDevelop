package com.popug.stoyalova.service;

import com.popug.stoyalova.event.UserChangeRoleEvent;
import com.popug.stoyalova.event.UserCudEvent;
import com.popug.stoyalova.event.UserEvent;
import com.popug.stoyalova.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask {

    private final UserStreamingProducer producerStream;
    private final UserBusinessProducer producerBus;

    public void sendStream(String topic, UserCudEvent event) {

        producerStream.sendMessage(topic, event);

        log.info(String.format("Produced: topic: %s value size: %s", topic,
                ObjectMapperUtils.toJson(event)));
    }

    public void sendBus(String topic, UserChangeRoleEvent event) {

        producerBus.sendMessage(topic, event);

        log.info(String.format("Produced: topic: %s value size: %s", topic,
                ObjectMapperUtils.toJson(event)));
    }
}
