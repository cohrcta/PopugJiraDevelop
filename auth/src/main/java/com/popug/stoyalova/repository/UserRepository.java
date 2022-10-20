package com.popug.stoyalova.repository;

import com.popug.stoyalova.model.user.UserData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserData, Long> {
    Optional<UserData> findByUsername(String name);

    Optional<UserData> findByPublicId(String publicId);

    List<UserData> findAllByRole(String role);
}
