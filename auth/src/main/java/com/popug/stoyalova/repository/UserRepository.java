package com.popug.stoyalova.repository;

import com.popug.stoyalova.model.user.UserData;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserData, Long> {
    Optional<UserData> findByUsername(String name);

    Optional<UserData> findByPublicId(String publicId);

    List<UserData> findAllByRole(String role);
}
