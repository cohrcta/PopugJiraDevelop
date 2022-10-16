package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.UserDto;
import com.popug.stoyalova.tasks.events.UserChangeRoleEvent;
import com.popug.stoyalova.tasks.events.UserCudEvent;
import com.popug.stoyalova.tasks.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @KafkaListener(topics = {"user.streaming"})
    public void consumeCud(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed CUD message -> TIMESTAMP: %d %s offset: %d key: %s " +
                " topic: %s", ts, message, offset, key,  topic));
        acknowledgment.acknowledge();
        UserCudEvent cudEvent = ObjectMapperUtils.toBean(message, UserCudEvent.class);
        if(cudEvent != null) {
            UserDto userDto = UserDto.builder()
                    .publicId(cudEvent.getEventData().getUserPublicId())
                    .role(cudEvent.getEventData().getRole())
                    .userName(cudEvent.getEventData().getUserName())
                    .build();
            if ("createUser".equals(cudEvent.getEventName())) {
                userService.save(userDto);
            } else if ("updateUser".equals(cudEvent.getEventName())){
                userService.update(userDto);
            }
        }
    }

    @KafkaListener(topics = {"user.BE"})
    public void consumeBE(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed BE message -> TIMESTAMP: %d %s offset: %d " +
                "topic: %s", ts, message, offset,  topic));
        acknowledgment.acknowledge();

        UserChangeRoleEvent beEvent = ObjectMapperUtils.toBean(message, UserChangeRoleEvent.class);
        if(beEvent != null) {
            UserDto userDto = UserDto.builder()
                    .publicId(beEvent.getEventData().getUserPublicId())
                    .role(beEvent.getEventData().getRole())
                    .build();
            if ("userRoleChanged".equals(beEvent.getEventName())) {
                userService.update(userDto);

            }
        }
    }
}
