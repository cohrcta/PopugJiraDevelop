package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.TaskDto;
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

    private final TaskRepository repository;
    private final UserService userService;

    @Override
    public void reassign() {
        List<Task> tasks = repository.findAllByCloseDateIsNull();
        List<User> developers = userService.findAllByRole("USER");
        tasks.forEach(task ->{ assignForRandom(developers, task);
           });

    }

    private void assignForRandom(List<User> developers, Task task){
        Collections.shuffle(developers, new Random());
        task.setUserAssign(developers.get(0));
        repository.save(task);
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
