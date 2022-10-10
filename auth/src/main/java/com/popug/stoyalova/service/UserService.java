package com.popug.stoyalova.service;

import com.popug.stoyalova.SecurityUser;
import com.popug.stoyalova.model.user.UserData;
import com.popug.stoyalova.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service(UserService.USER_DETAILS_SERVICE)
@RequiredArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    public static final String USER_DETAILS_SERVICE = "userDetailsServiceImpl";

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = repository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return SecurityUser.fromUserData(user);
    }

    public UserData loadUserDataByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
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
    public UserData save(UserData user) {
        return repository.save(user);
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
