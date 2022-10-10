package com.popug.stoyalova.tasks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserConsumer {

    @KafkaListener(topics = {"userCUD"})
    public void consumeCud(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed CUD message -> TIMESTAMP: %d %s offset: %d key: %s partition: " +
                "%d topic: %s", ts, message, offset, key, partition, topic));
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = {"userBE"})
    public void consumeBE(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed BE message -> TIMESTAMP: %d %s offset: %d partition: " +
                "%d topic: %s", ts, message, offset,  partition, topic));
        acknowledgment.acknowledge();
    }

}
