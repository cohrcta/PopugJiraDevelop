package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.model.Task;
import com.popug.stoyalova.accounts.model.User;

public interface ITaskService {
    Task save(Task task);

    Task update(Task task);

    Iterable<Task> findAllByStatus(String status);

    Iterable<Task> findAllByUser(User user);
}
