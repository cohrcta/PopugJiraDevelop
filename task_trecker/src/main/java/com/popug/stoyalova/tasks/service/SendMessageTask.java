package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.ErrorMessageDto;
import com.popug.stoyalova.tasks.events.TaskChangeEvent;
import com.popug.stoyalova.tasks.events.TaskCudEvent;
import com.popug.stoyalova.tasks.model.ErrorMessage;
import com.popug.stoyalova.tasks.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask {

    private final TaskStreamingProducer producerStream;
    private final TaskBusinessProducer producerBus;
    private final ErrorMessageService errorMessageService;

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

    @Scheduled(cron = "@hourly")
    private void trySendError(){
        List<ErrorMessageDto> errorMessages = errorMessageService.getAllNotSend();
        errorMessages.forEach(error ->{
               String topic = error.getTopic();
               if("task.streaming".equals(topic)){
                   TaskCudEvent event = ObjectMapperUtils.toBean(error.getMessage(),TaskCudEvent.class );
                   sendStream("task.streaming", event);
               }else if("task.BE".equals((topic))){
                   TaskChangeEvent event = ObjectMapperUtils.toBean(error.getMessage(),TaskChangeEvent.class );
                   sendBus("task.BE", event);

               }


        });
    }
}
