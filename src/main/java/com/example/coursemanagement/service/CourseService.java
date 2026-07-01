package com.example.coursemanagement.service;

import com.example.coursemanagement.config.PagedResponse;
import com.example.coursemanagement.dto.request.CreateCourseRequest;
import com.example.coursemanagement.dto.request.UpdateCourseRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.entity.enums.CourseStatus;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    PagedResponse<CourseResponse> getCourses(String search, Integer teacherId,
                                             CourseStatus status, Pageable pageable);
    CourseResponse getCourseById(Integer id);
    CourseResponse createCourse(CreateCourseRequest request);
    CourseResponse updateCourse(Integer id, UpdateCourseRequest request);
    CourseResponse updateStatus(Integer id, CourseStatus status);
    void deleteCourse(Integer id);
}