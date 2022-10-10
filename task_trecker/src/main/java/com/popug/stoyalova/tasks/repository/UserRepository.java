package com.popug.stoyalova.tasks.repository;

import com.popug.stoyalova.tasks.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String name);

    Optional<User> findByPublicId(String publicId);

    List<User> findAllByRole(String role);
}
