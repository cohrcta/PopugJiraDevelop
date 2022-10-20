package com.popug.stoyalova.accounts.repository;

import com.popug.stoyalova.accounts.model.Status;
import com.popug.stoyalova.accounts.model.Task;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {

    List<Task> findAllByStatus(Status status);

    Optional<Task> findByPublicId(String publicId);

}
