package com.example.coursemanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportTopCourseResponse {
    private Integer courseId;
    private String title;
    private String teacherName;
    private Long enrollmentCount;
    private String status;
}