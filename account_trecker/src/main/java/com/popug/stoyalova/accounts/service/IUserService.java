package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.model.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> findByPublicId(String publicId);

    User save(User user);
}
