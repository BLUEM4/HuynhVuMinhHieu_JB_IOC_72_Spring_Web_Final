package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.service.TokenBlacklistService;
import com.example.coursemanagement.dto.request.LoginRequest;
import com.example.coursemanagement.dto.response.AuthResponse;
import com.example.coursemanagement.dto.response.UserResponse;
import com.example.coursemanagement.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công", response));
    }

    // POST /api/auth/logout — thêm vào AuthController
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            tokenBlacklistService.addToBlacklist(token);
        }
        return ResponseEntity.ok(ApiResponse.ok("Đăng xuất thành công", null));
    }

    // POST /api/auth/verify
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verify(
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.startsWith("Bearer ")
                ? bearerToken.substring(7) : bearerToken;
        boolean valid = authService.verifyToken(token);
        if (!valid) {
            // Trả về mã HTTP 401 và gọi ApiResponse.fail()
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.fail("Token không hợp lệ hoặc đã hết hạn"));
        }

        // Trả về mã HTTP 200 và gọi ApiResponse.ok()
        return ResponseEntity.ok(ApiResponse.ok("Token hợp lệ", true));
    }


    // GET /api/auth/me
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = authService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(user));
    }


}