package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.dto.request.CreateNotificationRequest;
import com.example.coursemanagement.dto.response.NotificationResponse;
import com.example.coursemanagement.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // GET /api/notifications — lấy thông báo của mình
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                notificationService.getMyNotifications(userDetails.getUsername())));
    }

    // PUT /api/notifications/{id}/read — đánh dấu đã đọc
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("Đánh dấu đã đọc thành công",
                notificationService.markAsRead(id, userDetails.getUsername())));
    }

    // POST /api/notifications — ADMIN tạo thông báo
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.ok("Tạo thông báo thành công",
                notificationService.createNotification(request)));
    }

    // DELETE /api/notifications/{id} — ADMIN xóa thông báo
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa thông báo thành công", null));
    }
}