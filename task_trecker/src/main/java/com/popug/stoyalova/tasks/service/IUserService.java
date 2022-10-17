package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.UserDto;
import com.popug.stoyalova.tasks.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> findByPublicId(String publicId);

    List<User> findAllByRole(String role);

    void save(UserDto user);

    void update(UserDto user);
}
