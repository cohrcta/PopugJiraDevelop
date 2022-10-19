package com.popug.stoyalova.controller;

import com.popug.stoyalova.dto.UserDto;
import com.popug.stoyalova.exception.ValidateException;
import com.popug.stoyalova.model.user.UserData;
import com.popug.stoyalova.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
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
                .publicId(entity.getPublicId())
                .role(entity.getRole().name())
                .userName(entity.getUsername())
                .build();
    }

    @PostMapping
    public Map<String, Object> save(@RequestBody UserDto userDto) throws ValidateException {
        Long savedId = userService.save(userDto);
        return Map.of("id", savedId);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,@RequestBody UserDto userDto) throws ValidateException {
        userService.update(id, userDto);
    }

}
