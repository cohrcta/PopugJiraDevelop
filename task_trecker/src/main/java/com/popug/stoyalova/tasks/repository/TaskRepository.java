package com.popug.stoyalova.tasks.repository;

import com.popug.stoyalova.tasks.model.Status;
import com.popug.stoyalova.tasks.model.Task;
import com.popug.stoyalova.tasks.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

    List<Task> findAllByStatus(Status status);

    Optional<Task> findByPublicId(String publicId);

    List<Task> findAllByUserAssign(User user);
    List<Task> findAllByCloseDateIsNull();
}
