package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.TaskDto;
import com.popug.stoyalova.accounts.model.Task;
import com.popug.stoyalova.accounts.model.User;

import java.util.Optional;

public interface ITaskService {

    Task save(TaskDto taskDto);

    void update(TaskDto taskDto);

    Optional<Task> findByPublicId(String publicId);

}
