package com.dependencyimpact.serviceregistry.service;

import com.dependencyimpact.serviceregistry.dto.CreateUserRequest;
import com.dependencyimpact.serviceregistry.dto.CurrentUserResponse;
import com.dependencyimpact.serviceregistry.entity.User;
import com.dependencyimpact.serviceregistry.exception.DuplicateUserException;
import com.dependencyimpact.serviceregistry.mapper.UserMapper;
import com.dependencyimpact.serviceregistry.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public List<CurrentUserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public CurrentUserResponse createUser(CreateUserRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(existing -> {
            throw new DuplicateUserException("A user with email '" + request.getEmail() + "' already exists");
        });

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole().name());
        user.setStatus("ACTIVE");
        Instant now = Instant.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userMapper.toResponse(userRepository.save(user));
    }
}
