package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.TaskDto;
import com.popug.stoyalova.tasks.events.TaskChangeEvent;
import com.popug.stoyalova.tasks.events.TaskCudEvent;
import com.popug.stoyalova.tasks.exception.NotYourTask;
import com.popug.stoyalova.tasks.model.Status;
import com.popug.stoyalova.tasks.model.Task;
import com.popug.stoyalova.tasks.model.User;
import com.popug.stoyalova.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService{

    public static final String TASK_BE = "task.BE";
    private final TaskRepository repository;
    private final UserService userService;
    private final SendMessageTask  sendMessageTask;
    public static final String TASK_CUD = "task.streaming";

    @Override
    public void reassign() {
        List<Task> tasks = repository.findAllByCloseDateIsNull();
        List<User> developers = userService.findAllByRole("USER");
        tasks.forEach(task ->{ assignForRandom(developers, task);
            sendMessageTask.sendBus(TASK_BE, createBuilder("updateTask")
                    .eventData(TaskChangeEvent.TaskChangeData.builder()
                            .taskPublicId(task.getPublicId())
                            .userPublicId(task.getUserAssign().getPublicId())
                            .status(task.getStatus().name())
                            .taskChangeDate(new Date())
                            .build())
                    .build());});

    }

    private void assignForRandom(List<User> developers, Task task){
        Collections.shuffle(developers, new Random());
        task.setUserAssign(developers.get(0));
        repository.save(task);
    }

    private TaskCudEvent.TaskCudEventBuilder<?, ?> createMessage() {
        return TaskCudEvent.builder()
                .eventTime(new Date())
                .eventName("createTask")
                .eventVersion(1)
                .producer("taskService")
                .eventUID(UUID.randomUUID().toString());
    }


    private void assignOnCreateRandom(List<User> developers, Task.TaskBuilder task){
        Collections.shuffle(developers, new Random());
        task.userAssign(developers.get(0));
    }

    @Override
    public String save(TaskDto taskDto) {
        List<User> developers = userService.findAllByRole("USER");
        Optional<User> userOptional = userService.findByPublicId(taskDto.getUserCreated());
        if (userOptional.isEmpty()) {
            return null;
        }
        Task.TaskBuilder taskBuilder  = Task.builder()
                .publicId(UUID.randomUUID().toString())
                .createDate(new Date())
                .description(taskDto.getDescription())
                .status(Status.OPEN)
                .title(taskDto.getTitle())
                .userCreate(userOptional.get());

        assignOnCreateRandom(developers, taskBuilder);
        Task task = taskBuilder.build();
        repository.save(task);
        sendMessageTask.sendStream(TASK_CUD, createMessage()
                .eventData(TaskCudEvent.TaskCudData.builder()
                        .taskCreteDate(task.getCreateDate())
                        .userAssignPublicId(task.getUserAssign().getPublicId())
                        .taskPublicId(task.getPublicId())
                        .taskDescription(task.getDescription())
                        .taskTitle(task.getTitle())
                        .userCreatePublicId(task.getUserCreate().getPublicId())
                        .build())
                .build());
        sendMessageTask.sendBus(TASK_BE, createBuilder("updateTask")
                        .eventData(TaskChangeEvent.TaskChangeData.builder()
                                .taskPublicId(task.getPublicId())
                                .userPublicId(task.getUserAssign().getPublicId())
                                .status(task.getStatus().name())
                                .taskChangeDate(task.getCreateDate())
                                .build())
                .build());

        return task.getPublicId();
    }

    @Override
    public void update(TaskDto taskDto) {
        Task task = repository.findByPublicId(taskDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("Task with id '" + taskDto.getPublicId() + "' not found"));
        if (!task.getUserAssign().getPublicId().equals(taskDto.getUserAssign())){
            throw new NotYourTask("Task with id '" + taskDto.getPublicId() + "' not your." +
                    " Don't do work for "+ task.getUserAssign().getPublicId());
        }
        task.setCloseDate(new Date());
        task.setStatus(Status.CLOSE);
        repository.save(task);

        sendMessageTask.sendBus(TASK_BE, createBuilder("closeTask")
                .eventData(TaskChangeEvent.TaskChangeData.builder()
                        .taskPublicId(task.getPublicId())
                        .userPublicId(taskDto.getUserAssign())
                        .status(task.getStatus().name())
                        .taskChangeDate(task.getCloseDate())
                        .build())
                .build());


    }

    private TaskChangeEvent.TaskChangeEventBuilder<?, ?> createBuilder(String eventName) {
        return TaskChangeEvent.builder()
                .eventTime(new Date())
                .eventName(eventName)
                .eventVersion(1)
                .producer("taskService")
                .eventUID(UUID.randomUUID().toString());
    }

    @Override
    public List<Task> findAllByUserPublicId(String userPublicID) {
        Optional<User> userO = userService.findByPublicId(userPublicID);
        if (userO.isEmpty()) {
            return List.of();
        }
        User user = userO.get();
        if ("USER".equals(user.getRole())) {
            return repository.findAllByUserAssign(user);
        }
        return List.of();
    }
}
