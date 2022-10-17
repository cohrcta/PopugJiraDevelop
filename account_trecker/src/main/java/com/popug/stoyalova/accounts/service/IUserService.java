package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.UserDto;
import com.popug.stoyalova.accounts.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<User> findByPublicId(String publicId);

    User save(UserDto user);

    void update(UserDto user);

    void updateBalance(UserDto user);
}
