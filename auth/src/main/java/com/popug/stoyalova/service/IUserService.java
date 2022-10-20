package com.popug.stoyalova.service;

import com.popug.stoyalova.dto.UserDto;
import com.popug.stoyalova.exception.ValidateException;
import com.popug.stoyalova.model.user.UserData;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    Optional<UserData> findById(Long id);

    Optional<UserData> findByPublicId(String publicId);

    Long save(UserDto user) throws ValidateException;

    void update(Long id, UserDto user) throws ValidateException;

    List<UserData> findAll();

}
