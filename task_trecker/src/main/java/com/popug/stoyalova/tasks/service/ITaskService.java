package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.model.Task;
import com.popug.stoyalova.tasks.model.User;

public interface ITaskService {
    Task save(Task task);

    Task update(Task task);

    Iterable<Task> findAllByStatus(String status);

    Iterable<Task> findAllByUser(User user);
}
