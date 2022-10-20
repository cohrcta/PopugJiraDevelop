package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.ErrorMessageDto;
import com.popug.stoyalova.tasks.events.TaskCudEvent;
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
public class TaskStreamingProducer {

    private final ErrorMessageService errorMessageService;
    private final KafkaTemplate<String, TaskCudEvent> kafkaTemplate;

    @SneakyThrows
    public void sendMessage(String topic, TaskCudEvent message)
    {
        ListenableFuture<SendResult<String, TaskCudEvent>> sendResultListenableFuture = kafkaTemplate.send(topic, message);

        sendResultListenableFuture.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(@NotNull Throwable ex) {

                handleFailure(topic, message, ex);

            }

            @Override
            public void onSuccess(SendResult<String, TaskCudEvent> result) {

                handleSuccess(topic, message, result);

            }
        });


    }

    private void handleSuccess(String key, TaskCudEvent value, SendResult<String, TaskCudEvent> result) {

        log.info("The record for topic : {}, value : {} is produced successfully to offset {}", key, value, result.getRecordMetadata().offset());
        errorMessageService.markAsSend(value.getEventUID());
    }

    private void handleFailure(String key, TaskCudEvent value, Throwable ex) {

        log.info("The record for topic: {}, value: {} cannot be processed! caused by {}", key, value, ex.getMessage());
        errorMessageService.save(ErrorMessageDto.builder()
                .topic(key)
                .publicId(value.getEventUID())
                .message(ObjectMapperUtils.toJson(value))
                .eventName(value.getEventName())
                .description("Error send "+ value.getEventName())
                .build());
    }
}
