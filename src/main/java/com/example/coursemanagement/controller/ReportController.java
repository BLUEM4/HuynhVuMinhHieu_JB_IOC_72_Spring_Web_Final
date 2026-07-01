package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.dto.response.*;
import com.example.coursemanagement.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    // GET /api/reports/top_courses
    @GetMapping("/top_courses")
    public ResponseEntity<ApiResponse<List<ReportTopCourseResponse>>> getTopCourses() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.getTopCourses()));
    }

    // GET /api/reports/student_progress/{studentId}
    @GetMapping("/student_progress/{studentId}")
    public ResponseEntity<ApiResponse<ReportStudentProgressResponse>> getStudentProgress(
            @PathVariable Integer studentId) {
        return ResponseEntity.ok(ApiResponse.ok(
                reportService.getStudentProgress(studentId)));
    }

    // GET /api/reports/teacher_courses_overview/{teacherId}
    @GetMapping("/teacher_courses_overview/{teacherId}")
    public ResponseEntity<ApiResponse<ReportTeacherOverviewResponse>> getTeacherOverview(
            @PathVariable Integer teacherId) {
        return ResponseEntity.ok(ApiResponse.ok(
                reportService.getTeacherOverview(teacherId)));
    }
}