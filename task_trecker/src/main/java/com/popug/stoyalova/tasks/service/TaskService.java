package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.TaskDto;
import com.popug.stoyalova.tasks.events.TaskCloseEvent;
import com.popug.stoyalova.tasks.events.TaskCudEvent;
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

    private final TaskRepository repository;
    private final UserService userService;
    private final SendMessageTask  sendMessageTask;
    public static final String TASK_CUD = "taskCUD";

    @Override
    public void reassign() {
        List<Task> tasks = repository.findAllByCloseDateIsNull();
        List<User> developers = userService.findAllByRole("USER");
        tasks.forEach(task ->{ assignForRandom(developers, task);
            sendMessageTask.send(TASK_CUD, createMessage("updateTask")
                    .eventData(TaskCudEvent.TaskCudData.builder()
                            .userAssignPublicId(task.getUserAssign().getPublicId())
                            .taskPublicId(task.getPublicId())
                            .build())
                    .build());});

    }

    private void assignForRandom(List<User> developers, Task task){
        Collections.shuffle(developers, new Random());
        task.setUserAssign(developers.get(0));
        repository.save(task);
    }

    private TaskCudEvent.TaskCudEventBuilder<?, ?> createMessage(String eventName) {
        return TaskCudEvent.builder()
                .eventTime(new Date())
                .eventName(eventName)
                .eventVersion(1)
                .producer("taskService")
                .eventUID(UUID.randomUUID().toString());
    }

    @Override
    public String save(TaskDto taskDto) {
        List<User> developers = userService.findAllByRole("USER");
        Optional<User> userOptional = userService.findByPublicId(taskDto.getUserCreated());
        if (userOptional.isEmpty()) {
            return null;
        }
        Task task  = Task.builder()
                .publicId(UUID.randomUUID().toString())
                .createDate(new Date())
                .description(taskDto.getDescription())
                .status(Status.OPEN)
                .title(taskDto.getTitle())
                .userCreate(userOptional.get())
                .build();
        assignForRandom(developers, task);
        repository.save(task);

        sendMessageTask.send(TASK_CUD, createMessage("createTask")
                .eventData(TaskCudEvent.TaskCudData.builder()
                        .taskCreteDate(task.getCreateDate())
                        .taskPublicId(task.getPublicId())
                        .status(task.getStatus().name())
                        .taskDescription(task.getDescription())
                        .taskTitle(task.getTitle())
                        .userCreatePublicId(task.getUserCreate().getPublicId())
                        .userAssignPublicId(task.getUserAssign().getPublicId())
                        .build())
                .build());
        return task.getPublicId();
    }

    @Override
    public void update(TaskDto taskDto) {
        Task task = repository.findByPublicId(taskDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("Task with id '" + taskDto.getPublicId() + "' not found"));
        if (!task.getUserAssign().getPublicId().equals(taskDto.getUserAssign())){
            throw new RuntimeException("Task with id '" + taskDto.getPublicId() + "' not your." +
                    " Don't do work for "+ task.getUserAssign().getPublicId());
        }
        task.setCloseDate(new Date());
        task.setStatus(Status.CLOSE);
        repository.save(task);

        sendMessageTask.send("taskBE",TaskCloseEvent.builder()
                .eventTime(new Date())
                .eventName("closeTask")
                .eventVersion(1)
                .producer("taskService")
                .eventUID(UUID.randomUUID().toString())
                .eventData(TaskCloseEvent.TaskCloseData.builder()
                        .taskPublicId(task.getPublicId())
                        .status(task.getStatus().name())
                        .taskCloseDate(task.getCloseDate())
                        .build())
                .build());


    }

    @Override
    public List<Task> findAllByUserPublicId(String userPublicID) {
        Optional<User> userO = userService.findByPublicId(userPublicID);
        if (userO.isEmpty()) {
            return List.of();
        }
        User user = userO.get();
        if ("ADMIN".equals(user.getRole())) {
            return (List<Task>) repository.findAll();
        }
        if ("USER".equals(user.getRole())) {
            return repository.findAllByUserAssign(user);
        }
        return List.of();
    }
}
