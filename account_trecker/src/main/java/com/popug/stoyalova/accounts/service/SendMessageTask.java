package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.events.Event;
import com.popug.stoyalova.accounts.events.SalaryEvent;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.support.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageTask implements Job {

    private final SalaryProducer producer;
    private final UserService userService;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Job Salary --->>> Hello slaves! Time is " + new Date());
        List<User> users = userService.findAllByRole("USER");
        users.forEach(user ->
            send(SalaryEvent.builder()
                    .eventTime(new Date())
                    .eventName("salary")
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

    private void send( Event event) {

        producer.sendMessage("salary.BE", ObjectMapperUtils.toJson(event));

        log.info(String.format("Produced: topic: salary.BE value size: %s",
                ObjectMapperUtils.toJson(event)));
    }
}
