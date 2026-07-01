package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.example.coursemanagement.dto.response.EnrollmentResponse;
import com.example.coursemanagement.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // GET /api/enrollments — lấy danh sách enrollment của mình
    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                enrollmentService.getMyEnrollments(userDetails.getUsername())));
    }

    // POST /api/enrollments — đăng ký khóa học
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @Valid @RequestBody CreateEnrollmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(201).body(ApiResponse.ok(
                "Đăng ký khóa học thành công",
                enrollmentService.enroll(request, userDetails.getUsername())));
    }

    // GET /api/enrollments/{id} — chi tiết enrollment
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> getEnrollmentById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                enrollmentService.getEnrollmentById(id, userDetails.getUsername())));
    }

    // PUT /api/enrollments/{id}/complete_lesson/{lessonId}
    @PutMapping("/{id}/complete_lesson/{lessonId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> completeLesson(
            @PathVariable Integer id,
            @PathVariable Integer lessonId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                "Đánh dấu hoàn thành bài học thành công",
                enrollmentService.completeLesson(id, lessonId, userDetails.getUsername())));
    }
}