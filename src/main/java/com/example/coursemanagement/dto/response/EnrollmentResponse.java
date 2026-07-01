package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.entity.enums.EnrollmentStatus;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class EnrollmentResponse {
    private Integer enrollmentId;
    private UserResponse student;
    private CourseResponse course;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private BigDecimal progressPercentage;
    private LocalDateTime completionDate;
    private List<LessonProgressResponse> lessonProgresses;

    @Getter
    @Builder
    public static class LessonProgressResponse {
        private Integer lessonId;
        private String lessonTitle;
        private boolean completed;
        private LocalDateTime completedAt;
    }
}