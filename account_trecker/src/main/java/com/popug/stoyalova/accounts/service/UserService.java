package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository repository;

    @Override
    public Optional<User> findByPublicId(String publicId) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }
}
