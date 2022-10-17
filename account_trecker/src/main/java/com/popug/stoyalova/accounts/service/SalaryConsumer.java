package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.AuditDto;
import com.popug.stoyalova.accounts.dto.TaskDto;
import com.popug.stoyalova.accounts.dto.UserDto;
import com.popug.stoyalova.accounts.events.SalaryEvent;
import com.popug.stoyalova.accounts.events.TaskChangeEvent;
import com.popug.stoyalova.accounts.events.TaskCudEvent;
import com.popug.stoyalova.accounts.model.Status;
import com.popug.stoyalova.accounts.model.Task;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalaryConsumer {

    private final AccountService accountService;
    private final UserService userService;

    @KafkaListener(topics = {"salary.BE"})
    public void consumeBE(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed BE salary message -> TIMESTAMP: %d %s offset: %d " +
                "topic: %s", ts, message, offset,  topic));
        acknowledgment.acknowledge();

        SalaryEvent beEvent = ObjectMapperUtils.toBean(message, SalaryEvent.class);
        if(beEvent != null) {
            SalaryEvent.SalaryEventData taskChangeData = beEvent.getEventData();
            String uuid =UUID.randomUUID().toString();
            AuditDto.AuditDtoBuilder auditDtoBuilder = AuditDto.builder()
                    .publicId(uuid)
                    .userAssign(taskChangeData.getUserPublicId())
                    .creationDate(taskChangeData.getSalaryDate())
                    .logDate(new Date());

            Optional<User> userO =userService.findByPublicId(taskChangeData.getUserPublicId());
            checkUser(taskChangeData.getUserPublicId(), userO);

            if("salaryIsPay".equals(beEvent.getEventName())){ // after successful transaction!!
                auditDtoBuilder
                        .description("salary for user with ID " +
                                taskChangeData.getUserPublicId())
                        .salary(true)
                        .debit((int) Math.max(0, taskChangeData.getYourSalary()))
                        .credit(0);

                userService.updateBalance(UserDto.builder().publicId(uuid)
                        .money((int) Math.min(taskChangeData.getYourSalary(), 0)).build());

            }
            accountService.save(auditDtoBuilder.build());
        }
    }

    private void checkUser(String publicId, Optional<User> userO) {
        if(userO.isEmpty()) {
            UserDto userDto = UserDto.builder()
                    .publicId(publicId)
                    .build();
            userService.save(userDto);
        }
    }
}
