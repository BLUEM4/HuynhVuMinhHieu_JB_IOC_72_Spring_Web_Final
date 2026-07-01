package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CreateReviewRequest;
import com.example.coursemanagement.dto.request.UpdateReviewRequest;
import com.example.coursemanagement.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getReviewsByCourse(Integer courseId);
    ReviewResponse createReview(Integer courseId, CreateReviewRequest request, String username);
    ReviewResponse updateReview(Integer reviewId, UpdateReviewRequest request, String username);
    void deleteReview(Integer reviewId, String username);
}