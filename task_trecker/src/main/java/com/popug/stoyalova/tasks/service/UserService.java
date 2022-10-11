package com.popug.stoyalova.tasks.service;

import com.popug.stoyalova.tasks.dto.UserDto;
import com.popug.stoyalova.tasks.model.User;
import com.popug.stoyalova.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository repository;

    @Override
    public Optional<User> findByPublicId(String publicId) {
        return repository.findByPublicId(publicId);
    }

    @Override
    public List<User> findAllByRole(String role) {
        return repository.findAllByRole(role);
    }

    @Override
    public String save(UserDto userDto) {
        User user  = User.builder()
                .publicId(userDto.getPublicId())
                .role(userDto.getRole())
                .username(userDto.getUserName())
                .build();
        repository.save(user);
        return user.getPublicId();
    }

    @Override
    public void update(UserDto userDto) {
        User user = repository.findByPublicId(userDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("User with id '" + userDto.getPublicId() + "' not found"));
        user.setRole(userDto.getRole());
        user.setUsername(userDto.getUserName());
        repository.save(user);
    }
}
