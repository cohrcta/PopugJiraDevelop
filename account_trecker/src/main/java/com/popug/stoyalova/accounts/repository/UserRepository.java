package com.popug.stoyalova.accounts.repository;

import com.popug.stoyalova.accounts.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByPublicId(String publicId);

    List<User> findAllByRole(String role);
}
