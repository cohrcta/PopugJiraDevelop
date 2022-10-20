package com.popug.stoyalova.accounts.repository;

import com.popug.stoyalova.accounts.model.ErrorMessage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorMessageRepository extends PagingAndSortingRepository<ErrorMessage, Long> {
}
