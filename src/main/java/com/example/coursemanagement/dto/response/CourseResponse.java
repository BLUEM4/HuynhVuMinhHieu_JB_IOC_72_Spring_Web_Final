package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.entity.enums.CourseStatus;
import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CourseResponse {
    private Integer courseId;
    private String title;
    private String description;
    private UserResponse teacher;
    private BigDecimal price;
    private Integer durationHours;
    private CourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}