package com.popug.stoyalova.tasks.repository;

import com.popug.stoyalova.tasks.model.Status;
import com.popug.stoyalova.tasks.model.Task;
import com.popug.stoyalova.tasks.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

    List<Task> findAllByStatus(Status status);

    Optional<Task> findByPublicId(String publicId);

    List<Task> findAllByUser(User user);
}
