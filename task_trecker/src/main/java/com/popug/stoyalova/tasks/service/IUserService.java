package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.model.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> findByPublicId(String publicId);

    User save(User user);
}
