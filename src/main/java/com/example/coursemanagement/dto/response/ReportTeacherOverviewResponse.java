package com.example.coursemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReportTeacherOverviewResponse {
    private Integer teacherId;
    private String teacherName;
    private int totalCourses;
    private int totalStudents;
    private List<CourseOverviewItem> courses;

    @Getter
    @AllArgsConstructor
    public static class CourseOverviewItem {
        private Integer courseId;
        private String title;
        private String status;
        private Long enrollmentCount;
    }
}