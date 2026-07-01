package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.LoginRequest;
import com.example.coursemanagement.dto.response.AuthResponse;
import com.example.coursemanagement.dto.response.UserResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserResponse getMe(String username);
    boolean verifyToken(String token);
}