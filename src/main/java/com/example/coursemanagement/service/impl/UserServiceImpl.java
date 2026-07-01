package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.request.*;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.mapper.UserMapper;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers(Boolean status) {
        List<User> users = (status != null)
                ? userRepository.findByIsActive(status)
                : userRepository.findAll();
        return users.stream().map(userMapper::toResponse).toList();
    }

    @Override
    public UserResponse getUserById(Integer id) {
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw AppException.conflict("Username đã tồn tại");
        if (userRepository.existsByEmail(request.getEmail()))
            throw AppException.conflict("Email đã tồn tại");

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole() != null ? request.getRole() : UserRole.STUDENT)
                .isActive(true)
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Integer id, UpdateUserRequest request) {
        User user = findUserById(id);

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail()))
            throw AppException.conflict("Email đã được sử dụng");

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void updatePassword(Integer id, UpdatePasswordRequest request) {
        User user = findUserById(id);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash()))
            throw AppException.badRequest("Mật khẩu cũ không đúng");

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse updateRole(Integer id, UserRole role) {
        User user = findUserById(id);

        // ADMIN không được đổi role của ADMIN khác
        if (user.getRole() == UserRole.ADMIN) {
            throw AppException.forbidden("Không thể thay đổi role của ADMIN khác");
        }

        user.setRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateStatus(Integer id, boolean isActive) {
        User user = findUserById(id);
        user.setActive(isActive);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id))
            throw AppException.notFound("User không tồn tại");
        userRepository.deleteById(id);
    }

    private User findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));
    }
}