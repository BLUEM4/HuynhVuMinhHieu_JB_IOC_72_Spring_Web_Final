package com.example.coursemanagement.mapper;

import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final UserMapper userMapper;

    public CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .teacher(userMapper.toResponse(course.getTeacher()))
                .price(course.getPrice())
                .durationHours(course.getDurationHours())
                .status(course.getStatus())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}