package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.events.SalaryEvent;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask {

    private final SalaryProducer producer;
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void execute() {
        System.out.println("Job Salary --->>> Hello slaves! Time is " + new Date());
        List<User> users = userService.findAllByRole("USER");
        users.forEach(user ->
            send(SalaryEvent.builder()
                    .eventTime(new Date())
                    .eventName("salaryFormed")
                    .eventVersion(1)
                    .producer("accountService")
                    .eventUID(UUID.randomUUID().toString())
                    .eventData(SalaryEvent.SalaryEventData.builder()
                            .salaryDate(new Date())
                            .userPublicId(user.getPublicId())
                            .yourSalary(user.getBalance())
                            .build())
                    .build()));
    }

    private void send( SalaryEvent event) {

        producer.sendMessage("salary.BE", event);

        log.info(String.format("Produced: topic: salary.BE value size: %s",
                ObjectMapperUtils.toJson(event)));
    }
}
