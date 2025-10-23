package org.personalblogapi.service;

import org.personalblogapi.dto.LoginRequest;
import org.personalblogapi.dto.RegisterRequest;
import org.personalblogapi.model.entity.User;

public interface AuthService {
    public User register(RegisterRequest request);

    public User login(LoginRequest request);
}
