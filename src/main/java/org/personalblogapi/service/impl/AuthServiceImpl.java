package org.personalblogapi.service.impl;

import lombok.RequiredArgsConstructor;

import org.personalblogapi.dto.LoginRequest;
import org.personalblogapi.dto.RegisterRequest;
import org.personalblogapi.exception.ConflictException;
import org.personalblogapi.model.entity.User;
import org.personalblogapi.repository.UserRepository;
import org.personalblogapi.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(RegisterRequest request) {
        // Validate
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username " + request.getUsername() + " is already in use");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email " + request.getEmail() + " is already in use");
        }

        // Create user with hashed password
        User user = User.builder().username(request.getUsername()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .createdAt(LocalDateTime.now())
                .isRegistered(true)
                .fullName(request.getFirstName() + " " + request.getLastName()).build();

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new ConflictException("Invalid credentials");
        }

        User user = userOpt.get();

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ConflictException("Invalid credentials");
        }
        return user;
    }
}
