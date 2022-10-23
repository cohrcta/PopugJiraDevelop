package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.events.TaskChangeEvent;
import com.popug.stoyalova.tasks.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskBusinessProducer {

    private final KafkaTemplate<String, TaskChangeEvent> kafkaTemplate;

    @SneakyThrows
    public void sendMessage(String topic, TaskChangeEvent message) {
        ListenableFuture<SendResult<String, TaskChangeEvent>> sendResultListenableFuture = kafkaTemplate.send(topic, message);

        sendResultListenableFuture.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(@NotNull Throwable ex) {

                handleFailure(topic, message, ex);

            }

            @Override
            public void onSuccess(SendResult<String, TaskChangeEvent> result) {

                handleSuccess(topic, message, result);

            }
        });


    }

    private void handleSuccess(String key, TaskChangeEvent value, SendResult<String, TaskChangeEvent> result) {

        log.info("The record for topic : {}, value : {} is produced sucessfullly to offset {}", key, value, result.getRecordMetadata().offset());
    }

    private void handleFailure(String key, TaskChangeEvent value, Throwable ex) {

        log.info("The record for topic: {}, value: {} cannot be processed! caused by {}", key, value, ex.getMessage());
    }
}
