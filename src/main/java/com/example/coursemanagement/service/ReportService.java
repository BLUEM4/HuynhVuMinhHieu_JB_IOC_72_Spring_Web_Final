package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.response.ReportStudentProgressResponse;
import com.example.coursemanagement.dto.response.ReportTeacherOverviewResponse;
import com.example.coursemanagement.dto.response.ReportTopCourseResponse;
import java.util.List;

public interface ReportService {
    List<ReportTopCourseResponse> getTopCourses();
    ReportStudentProgressResponse getStudentProgress(Integer studentId);
    ReportTeacherOverviewResponse getTeacherOverview(Integer teacherId);
}