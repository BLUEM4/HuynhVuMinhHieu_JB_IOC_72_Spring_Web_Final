package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.request.CreateReviewRequest;
import com.example.coursemanagement.dto.request.UpdateReviewRequest;
import com.example.coursemanagement.dto.response.ReviewResponse;
import com.example.coursemanagement.entity.*;
import com.example.coursemanagement.entity.enums.EnrollmentStatus;
import com.example.coursemanagement.entity.enums.UserRole;
import com.example.coursemanagement.mapper.ReviewMapper;
import com.example.coursemanagement.repository.*;
import com.example.coursemanagement.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCourse(Integer courseId) {
        findCourseById(courseId);
        return reviewRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponse createReview(Integer courseId, CreateReviewRequest request,
                                       String username) {
        Course course = findCourseById(courseId);
        User student = findUserByUsername(username);

        // Chỉ STUDENT đã enrolled mới được review
        var enrollment = enrollmentRepository
                .findByStudent_UserIdAndCourse_CourseId(student.getUserId(), courseId)
                .orElseThrow(() -> AppException.badRequest(
                        "Bạn chưa đăng ký khóa học này"));

        // Kiểm tra đã review chưa
        if (reviewRepository.existsByCourse_CourseIdAndStudent_UserId(
                courseId, student.getUserId())) {
            throw AppException.conflict("Bạn đã đánh giá khóa học này rồi");
        }

        Review review = Review.builder()
                .course(course)
                .student(student)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Integer reviewId, UpdateReviewRequest request,
                                       String username) {
        Review review = findReviewById(reviewId);
        User user = findUserByUsername(username);

        // Chỉ chính chủ hoặc ADMIN mới được sửa
        if (user.getRole() != UserRole.ADMIN &&
                !review.getStudent().getUserId().equals(user.getUserId())) {
            throw AppException.forbidden("Bạn không có quyền sửa đánh giá này");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId, String username) {
        Review review = findReviewById(reviewId);
        User user = findUserByUsername(username);

        if (user.getRole() != UserRole.ADMIN &&
                !review.getStudent().getUserId().equals(user.getUserId())) {
            throw AppException.forbidden("Bạn không có quyền xóa đánh giá này");
        }

        reviewRepository.deleteById(reviewId);
    }

    private Course findCourseById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("Course không tồn tại"));
    }

    private Review findReviewById(Integer id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("Review không tồn tại"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));
    }
}