package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.TaskDto;
import com.popug.stoyalova.tasks.model.Task;
import com.popug.stoyalova.tasks.model.User;

import java.util.List;

public interface ITaskService {
    void reassign();

    String save(TaskDto taskDto);

    void update(TaskDto taskDto);

    List<Task> findAllByUserPublicId(String userPublicID);
}
