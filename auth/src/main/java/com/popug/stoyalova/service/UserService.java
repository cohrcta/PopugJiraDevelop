package com.popug.stoyalova.service;

import com.popug.stoyalova.SecurityUser;
import com.popug.stoyalova.event.UserEvent;
import com.popug.stoyalova.exception.ValidateException;
import com.popug.stoyalova.model.user.Role;
import com.popug.stoyalova.model.user.UserData;
import com.popug.stoyalova.model.user.UserDto;
import com.popug.stoyalova.repository.UserRepository;
import com.popug.stoyalova.support.BeanValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service(UserService.USER_DETAILS_SERVICE)
@RequiredArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    public static final String USER_DETAILS_SERVICE = "userDetailsServiceImpl";
    public static final String USER_CUD = "userCUD";

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final SendMessageTask  sendMessageTask;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = repository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        sendMessageTask.send(USER_CUD, UserEvent.builder()
                .eventTime(new Date())
                .eventName("TestUserCUD")
                .eventVersion(1)
                .producer("authService")
                .eventUID(UUID.randomUUID().toString()).build());
        sendMessageTask.send("userBE", UserEvent.builder()
                .eventTime(new Date())
                .eventName("TestUserBE")
                .eventVersion(1)
                .producer("authService")
                .eventUID(UUID.randomUUID().toString()).build());

        return SecurityUser.fromUserData(user);
    }

    @Override
    public Optional<UserData> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserData> findByPublicId(String publicId) {
        return repository.findByPublicId(publicId);
    }

    @Override
    public Long save(UserDto userDto)throws ValidateException {
        Validate.notNull(userDto, "Input date cannot be null");
        validateUserDto(userDto);
        UserData user  = UserData.builder()
                .createDate(new Date())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .publicId(UUID.randomUUID().toString())
                .role(Role.valueOf(userDto.getRole()))
                .username(userDto.getUserName())
                .build();
        repository.save(user);
        return user.getId();
    }

    @Override
    public UserData update(Long id, UserDto userDto) throws ValidateException {
        Validate.notNull(id, "Template id should be set");
        UserData user = repository.findById(id)
                .orElseThrow(() -> new ValidateException(List.of(
                         ValidateException.FieldValidationError.builder()
        .message("User with id '" + id + "' not found").build())));
        validateUserDto(userDto);
        user.setUpdateDate(new Date());
        user.setEmail(userDto.getEmail());
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setName(userDto.getName());
        user.setUsername(userDto.getUserName());

        return repository.save(user);
    }

    private void validateUserDto(UserDto userDto) throws ValidateException {
        BeanValidator.validateOrThrow(userDto);
    }

    @Override
    public List<UserData> findAllByRole(String role) {
        return repository.findAllByRole(role);
    }

    @Override
    public List<UserData> findAll() {
        return (List<UserData>) repository.findAll();
    }
}
