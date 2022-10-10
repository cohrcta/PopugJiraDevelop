package com.popug.stoyalova.service;

import com.popug.stoyalova.model.user.UserData;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<UserData> findById(Long id);

    Optional<UserData> findByPublicId(String publicId);

    UserData save(UserData user);

    List<UserData> findAllByRole(String role);

    List<UserData> findAll();

}
