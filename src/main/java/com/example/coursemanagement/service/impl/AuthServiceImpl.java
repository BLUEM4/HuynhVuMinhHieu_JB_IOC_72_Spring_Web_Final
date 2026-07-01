package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.config.JwtTokenProvider;
import com.example.coursemanagement.dto.request.LoginRequest;
import com.example.coursemanagement.dto.response.AuthResponse;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw AppException.unauthorized("Username hoặc password không đúng");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));

        String token = jwtTokenProvider.generateToken(user.getUsername());

        return new AuthResponse(token, toUserResponse(user));
    }

    @Override
    public UserResponse getMe(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));
        return toUserResponse(user);
    }

    @Override
    public boolean verifyToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}