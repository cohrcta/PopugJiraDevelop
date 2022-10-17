package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.TaskDto;
import com.popug.stoyalova.accounts.model.Task;
import com.popug.stoyalova.accounts.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService{

    private final TaskRepository repository;

    @Override
    public Task save(TaskDto taskDto) {
        Task.TaskBuilder taskBuilder  = Task.builder()
                .publicId(taskDto.getPublicId())
                .createDate(taskDto.getCreationDate())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .amount(taskDto.getAmount())
                .fee(taskDto.getFee())
                .userCreatePublicId(taskDto.getUserCreated());
        Task task = taskBuilder.build();
        repository.save(task);
        return task;
    }

    @Override
    public void update(TaskDto taskDto) {
        Task task = repository.findByPublicId(taskDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("Task with id '" + taskDto.getPublicId() + "' not found"));
        task.setStatus(taskDto.getStatus());
        task.setCloseDate(taskDto.getChangeDate());
        repository.save(task);
    }

    @Override
    public Optional<Task> findByPublicId(String publicId) {
        return repository.findByPublicId(publicId);
    }

}
