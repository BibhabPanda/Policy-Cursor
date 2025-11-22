package com.mercury.pas.service;

import com.mercury.pas.model.dto.AuthResponse;
import com.mercury.pas.model.dto.LoginRequest;
import com.mercury.pas.model.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}

