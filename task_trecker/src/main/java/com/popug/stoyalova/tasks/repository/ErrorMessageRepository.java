package com.popug.stoyalova.tasks.repository;

import com.popug.stoyalova.tasks.model.ErrorMessage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ErrorMessageRepository extends PagingAndSortingRepository<ErrorMessage, Long> {

    List<ErrorMessage> findAllBySendTimeIsNull();
    Optional<ErrorMessage> findByPublicId(String publicId);
}
