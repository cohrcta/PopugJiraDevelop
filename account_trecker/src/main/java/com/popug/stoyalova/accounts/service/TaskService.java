package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.model.Task;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.repository.TaskRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskService implements ITaskService{

    private final TaskRepository repository;

    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public Iterable<Task> findAllByStatus(String status) {
        return null;
    }

    @Override
    public Iterable<Task> findAllByUser(User user) {
        return null;
    }
}
