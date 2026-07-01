package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CreateEnrollmentRequest;
import com.example.coursemanagement.dto.response.EnrollmentResponse;
import java.util.List;

public interface EnrollmentService {
    List<EnrollmentResponse> getMyEnrollments(String username);
    EnrollmentResponse getEnrollmentById(Integer enrollmentId, String username);
    EnrollmentResponse enroll(CreateEnrollmentRequest request, String username);
    EnrollmentResponse completeLesson(Integer enrollmentId, Integer lessonId, String username);
}