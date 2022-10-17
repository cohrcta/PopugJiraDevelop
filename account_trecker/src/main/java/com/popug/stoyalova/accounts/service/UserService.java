package com.popug.stoyalova.accounts.service;

import com.popug.stoyalova.accounts.dto.UserDto;
import com.popug.stoyalova.accounts.model.User;
import com.popug.stoyalova.accounts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository repository;

    @Override
    public Optional<User> findByPublicId(String publicId) {
        return repository.findByPublicId(publicId);
    }

    @Override
    public User save(UserDto userDto) {
        User user  = User.builder()
                .publicId(userDto.getPublicId())
                .role(userDto.getRole())
                .username(userDto.getUserName())
                .build();
        repository.save(user);
        return user;
    }

    @Override
    public void update(UserDto userDto) {
        User user = repository.findByPublicId(userDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("User with id '" + userDto.getPublicId() + "' not found"));
        user.setRole(userDto.getRole());
        user.setUsername(userDto.getUserName());
        repository.save(user);
    }

    @Override
    public void updateBalance(UserDto userDto) {
        User user = repository.findByPublicId(userDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("User with id '" + userDto.getPublicId() + "' not found"));
        user.setBalance(user.getBalance()+userDto.getMoney());
        repository.save(user);
    }

    @Override
    public List<User> findAllByRole(String role) {
        return repository.findAllByRole(role);
    }

}
