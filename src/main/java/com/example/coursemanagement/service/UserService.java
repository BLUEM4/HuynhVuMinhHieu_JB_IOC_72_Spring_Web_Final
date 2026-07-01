package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CreateUserRequest;
import com.example.coursemanagement.dto.request.UpdateUserRequest;
import com.example.coursemanagement.dto.request.UpdatePasswordRequest;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.enums.UserRole;
import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers(Boolean status);
    UserResponse getUserById(Integer id);
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Integer id, UpdateUserRequest request);
    void updatePassword(Integer id, UpdatePasswordRequest request);
    UserResponse updateRole(Integer id, UserRole role);
    UserResponse updateStatus(Integer id, boolean isActive);
    void deleteUser(Integer id);
}