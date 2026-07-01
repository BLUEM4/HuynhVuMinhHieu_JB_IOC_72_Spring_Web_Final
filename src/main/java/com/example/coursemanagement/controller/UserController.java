package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.dto.request.*;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // GET /api/users — ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(required = false) Boolean status) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllUsers(status)));
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserById(id)));
    }

    // POST /api/users — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.ok("Tạo user thành công", userService.createUser(request)));
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thành công",
                userService.updateUser(id, request)));
    }

    // PUT /api/users/{id}/password
    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Đổi mật khẩu thành công", null));
    }

    // PUT /api/users/{id}/role
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRoleRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật role thành công",
                userService.updateRole(id, request.getRole())));
    }

    // PUT /api/users/{id}/status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật status thành công",
                userService.updateStatus(id, request.getIsActive())));
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa user thành công", null));
    }
}