package com.popug.stoyalova.controller;

import com.popug.stoyalova.SecurityUser;
import com.popug.stoyalova.model.user.UserData;
import com.popug.stoyalova.model.user.UserDto;
import com.popug.stoyalova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.findAll().stream().map(this :: entityMapper).collect(Collectors.toList());
    }
    private UserDto entityMapper( UserData entity){
        return UserDto.builder()
                .name(entity.getName())
                .dateCreate((Timestamp) entity.getCreateDate())
                .email(entity.getEmail())
                .role(entity.getRole().name())
                .userName(entity.getUsername())
                .build();
    }
}
