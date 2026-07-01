package com.example.coursemanagement.mapper;

import com.example.coursemanagement.dto.response.ReviewResponse;
import com.example.coursemanagement.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final UserMapper userMapper;

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .courseId(review.getCourse().getCourseId())
                .courseTitle(review.getCourse().getTitle())
                .student(userMapper.toResponse(review.getStudent()))
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}