package com.example.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private Integer reviewId;
    private Integer courseId;
    private String courseTitle;
    private UserResponse student;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}