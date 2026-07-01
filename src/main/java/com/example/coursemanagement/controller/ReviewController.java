package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.dto.request.CreateReviewRequest;
import com.example.coursemanagement.dto.request.UpdateReviewRequest;
import com.example.coursemanagement.dto.response.ReviewResponse;
import com.example.coursemanagement.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // GET /api/courses/{courseId}/reviews
    @GetMapping("/api/courses/{courseId}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviews(
            @PathVariable Integer courseId) {
        return ResponseEntity.ok(ApiResponse.ok(
                reviewService.getReviewsByCourse(courseId)));
    }

    // POST /api/courses/{courseId}/reviews
    @PostMapping("/api/courses/{courseId}/reviews")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Integer courseId,
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(201).body(ApiResponse.ok("Đánh giá thành công",
                reviewService.createReview(courseId, request, userDetails.getUsername())));
    }

    // PUT /api/reviews/{reviewId}
    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Integer reviewId,
            @Valid @RequestBody UpdateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật đánh giá thành công",
                reviewService.updateReview(reviewId, request, userDetails.getUsername())));
    }

    // DELETE /api/reviews/{reviewId}
    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Integer reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Xóa đánh giá thành công", null));
    }
}