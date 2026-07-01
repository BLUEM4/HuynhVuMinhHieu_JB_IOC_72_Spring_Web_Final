package com.example.coursemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReportStudentProgressResponse {
    private Integer studentId;
    private String studentName;
    private int totalEnrolled;
    private int totalCompleted;
    private List<CourseProgressItem> courses;

    @Getter
    @AllArgsConstructor
    public static class CourseProgressItem {
        private Integer courseId;
        private String courseTitle;
        private BigDecimal progressPercentage;
        private String status;
    }
}