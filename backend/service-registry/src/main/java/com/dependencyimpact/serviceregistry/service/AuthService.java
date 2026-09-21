package com.dependencyimpact.serviceregistry.service;

import com.dependencyimpact.common.model.Role;
import com.dependencyimpact.common.security.JwtTokenProvider;
import com.dependencyimpact.serviceregistry.dto.CurrentUserResponse;
import com.dependencyimpact.serviceregistry.dto.LoginRequest;
import com.dependencyimpact.serviceregistry.dto.LoginResponse;
import com.dependencyimpact.serviceregistry.entity.User;
import com.dependencyimpact.serviceregistry.exception.InvalidCredentialsException;
import com.dependencyimpact.serviceregistry.exception.UserNotFoundException;
import com.dependencyimpact.serviceregistry.mapper.UserMapper;
import com.dependencyimpact.serviceregistry.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@org.springframework.stereotype.Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    public LoginResponse login(LoginRequest request) {
        // Deliberately the same error for "no such user" and "wrong password" -
        // docs/security.md #20 says not to reveal whether a username exists.
        User user = userRepository.findByEmail(request.getEmail())
                .filter(candidate -> "ACTIVE".equals(candidate.getStatus()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        String accessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), Role.valueOf(user.getRole()));
        return new LoginResponse(accessToken, "Bearer", jwtTokenProvider.getExpirationSeconds(), userMapper.toResponse(user));
    }

    public CurrentUserResponse getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return userMapper.toResponse(user);
    }
}
