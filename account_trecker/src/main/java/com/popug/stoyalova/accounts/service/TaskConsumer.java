package com.popug.stoyalova.accounts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskConsumer {

    private final AccountService accountService;
    private final TaskService taskService;
    private final UserService userService;
    private final Random random = new Random();

    @KafkaListener(topics = {"task.streaming"})
    public void consumeCud( ConsumerRecord<String, List<TaskCudEvent>> cr,
        @Payload List<TaskCudEvent> payload,
        final @Header(KafkaHeaders.OFFSET) Integer offset,
        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) throws JsonProcessingException {
            log.info("#### -> Consumed Stream task message -> 1 [JSON] received key {}:" +
                            " Topic [{}] | Payload: {} | Record: {} | TIMESTAMP: {}| {} offset",
                    cr.key(), topic, payload, cr.toString(), ts, offset);
        ObjectMapper objectMapper = ObjectMapperUtils.objectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload.toString());
        log.info("JsonNode {}", jsonNode);
        List<TaskCudEvent> taskCudEvents = objectMapper.convertValue(jsonNode,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TaskCudEvent.class));
        TaskCudEvent cudEvent = taskCudEvents.get(0);
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

    @SneakyThrows
    @KafkaListener(topics = {"task.BE"})
    public void consumeBE(ConsumerRecord<String, List<TaskChangeEvent>> cr,
                          @Payload List<TaskChangeEvent> payload,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info("#### -> Consumed BE task message -> 1 [JSON] received key {}:" +
                        " Topic [{}] | Payload: {} | Record: {} | TIMESTAMP: {}| {} offset",
                cr.key(), topic, payload, cr.toString(), ts, offset);

        ObjectMapper objectMapper = ObjectMapperUtils.objectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload.toString());
        log.info("JsonNode {}", jsonNode);
        List<TaskChangeEvent> beEvents = objectMapper.convertValue(jsonNode,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, TaskChangeEvent.class));
        TaskChangeEvent beEvent = beEvents.get(0);
        if(beEvent != null) {
            TaskChangeEvent.TaskChangeData taskChangeData = beEvent.getEventData();
            AuditDto.AuditDtoBuilder auditDtoBuilder = AuditDto.builder()
                    .taskPublicId(taskChangeData.getTaskPublicId())
                    .userAssign(taskChangeData.getUserPublicId())
                    .creationDate(taskChangeData.getTaskChangeDate())
                    .logDate(new Date());

            Optional<Task> taskO =taskService.findByPublicId(taskChangeData.getTaskPublicId());
            Optional<User> userO =userService.findByPublicId(taskChangeData.getUserPublicId());
            Task task = getTask(taskChangeData, taskO);
            checkUser(taskChangeData.getUserPublicId(), userO);

            if ("updateTask".equals(beEvent.getEventName())) {

                auditDtoBuilder
                        .description("assign Task with ID " +taskChangeData.getTaskPublicId()+ " on user with ID " +
                                taskChangeData.getUserPublicId())
                        .salary(false)
                        .credit(task.getAmount())
                        .debit(0);
                taskService.update(TaskDto.builder().publicId(taskChangeData.getTaskPublicId()).status(Status.REASSIGN).build());

                userService.updateBalance(UserDto.builder().publicId(taskChangeData.getUserPublicId())
                .money(task.getAmount()).build());

            } else if("closeTask".equals(beEvent.getEventName())){

                auditDtoBuilder
                        .description("close Task with ID " +taskChangeData.getTaskPublicId()+ " by user with ID " +
                                taskChangeData.getUserPublicId())
                        .salary(false)
                        .debit(task.getFee())
                        .credit(0);

                userService.updateBalance(UserDto.builder().publicId(taskChangeData.getUserPublicId())
                        .money(task.getFee()).build());

                taskService.update(TaskDto.builder().status(Status.CLOSE).publicId(taskChangeData.getTaskPublicId())
                        .changeDate(taskChangeData.getTaskChangeDate()).build());

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
