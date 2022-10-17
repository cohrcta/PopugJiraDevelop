package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.AuditDto;
import com.popug.stoyalova.accounts.dto.TaskDto;
import com.popug.stoyalova.accounts.dto.UserDto;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskConsumer {

    private final AccountService accountService;
    private final TaskService taskService;
    private final UserService userService;
    private final Random random = new Random();

    @KafkaListener(topics = {"task.streaming"})
    public void consumeCud(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed CUD task message -> TIMESTAMP: %d %s offset: %d key: %s " +
                " topic: %s", ts, message, offset, key,  topic));
        acknowledgment.acknowledge();
        TaskCudEvent cudEvent = ObjectMapperUtils.toBean(message, TaskCudEvent.class);

        if(cudEvent != null) {
            TaskDto taskDto = fillTaskDtoForCreate(cudEvent.getEventData());
            if ("createTask".equals(cudEvent.getEventName())) {
                Optional<Task> taskO =taskService.findByPublicId(cudEvent.getEventData().getTaskPublicId());
                if(taskO.isEmpty()) {
                    taskService.save(taskDto);
                }else{
                    taskService.update(taskDto);
                }
            }
        }
    }

    private TaskDto fillTaskDtoForCreate( TaskCudEvent.TaskCudData taskCudData){

        int amount = random.ints(-20, -10)
                .findFirst().orElse(-10);
        int fee = random.ints(20, 40)
                .findFirst().orElse(20);
        return TaskDto.builder()
                .publicId(taskCudData.getTaskPublicId())
                .creationDate(taskCudData.getTaskCreteDate())
                .description(taskCudData.getTaskDescription())
                .status(Status.OPEN)
                .title(taskCudData.getTaskTitle())
                .userCreated(taskCudData.getUserCreatePublicId())
                .amount(amount)
                .fee(fee)
                .build();
    }

    @KafkaListener(topics = {"task.BE"})
    public void consumeBE(final @Payload String message,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) {
        log.info(String.format("#### -> Consumed BE task message -> TIMESTAMP: %d %s offset: %d " +
                "topic: %s", ts, message, offset,  topic));
        acknowledgment.acknowledge();

        TaskChangeEvent beEvent = ObjectMapperUtils.toBean(message, TaskChangeEvent.class);
        if(beEvent != null) {
            TaskChangeEvent.TaskChangeData taskChangeData = beEvent.getEventData();
            AuditDto.AuditDtoBuilder auditDtoBuilder = AuditDto.builder()
                    .publicId(taskChangeData.getTaskPublicId())
                    .userAssign(taskChangeData.getUserPublicId())
                    .creationDate(taskChangeData.getTaskChangeDate())
                    .logDate(new Date());

            Optional<Task> taskO =taskService.findByPublicId(taskChangeData.getTaskPublicId());
            Optional<User> userO =userService.findByPublicId(taskChangeData.getUserPublicId());
            Task task = getTask(taskChangeData, taskO);
            checkUser(taskChangeData.getUserPublicId(), userO);

            if ("updateTask".equals(beEvent.getEventName())) { //назначили

                auditDtoBuilder
                        .description("assign Task with ID " +taskChangeData.getTaskPublicId()+ " on user with ID " +
                                taskChangeData.getUserPublicId())
                        .salary(false)
                        .credit(task.getAmount())
                        .debit(0);
                taskService.update(TaskDto.builder().status(Status.REASSIGN).build());

                userService.updateBalance(UserDto.builder().publicId(taskChangeData.getTaskPublicId())
                .money(task.getAmount()).build());

            } else if("closeTask".equals(beEvent.getEventName())){ // закрыл

                auditDtoBuilder
                        .description("close Task with ID " +taskChangeData.getTaskPublicId()+ " by user with ID " +
                                taskChangeData.getUserPublicId())
                        .salary(false)
                        .debit(task.getFee())
                        .credit(0);

                userService.updateBalance(UserDto.builder().publicId(taskChangeData.getTaskPublicId())
                        .money(task.getFee()).build());

                taskService.update(TaskDto.builder().status(Status.CLOSE)
                        .changeDate(taskChangeData.getTaskChangeDate()).build());

            } else if("salary".equals(beEvent.getEventName())){ //выплата зарплаты
            // TODO   добавить в другой консьюмер? update after salary module
                auditDtoBuilder
                        .description("salary for user with ID " +
                                taskChangeData.getUserPublicId())
                        .salary(true)
                        .debit(0)
                        .credit(0);

                userService.updateBalance(UserDto.builder().publicId(taskChangeData.getTaskPublicId())
                        .money(0).build());

            }
            accountService.save(auditDtoBuilder.build());
        }
    }

    private Task getTask(TaskChangeEvent.TaskChangeData taskChangeData, Optional<Task> taskO) {
        Task task;
        if(taskO.isEmpty()) {
            TaskDto taskDto = fillTaskDtoForCreate(TaskCudEvent.TaskCudData.builder()
                    .taskPublicId(taskChangeData.getTaskPublicId())
                    .build());
            task = taskService.save(taskDto);
        }else{
            task = taskO.get();
        }
        return task;
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
