package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.config.PagedResponse;
import com.example.coursemanagement.dto.request.CreateCourseRequest;
import com.example.coursemanagement.dto.request.UpdateCourseRequest;
import com.example.coursemanagement.dto.request.UpdateCourseStatusRequest;
import com.example.coursemanagement.dto.response.CourseResponse;
import com.example.coursemanagement.entity.enums.CourseStatus;
import com.example.coursemanagement.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // GET /api/courses?search=&teacherId=&status=&page=1&size=10
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<CourseResponse>>> getCourses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer teacherId,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable = PageRequest.of(
                page - 1, size, Sort.by("createdAt").descending());

        return ResponseEntity.ok(ApiResponse.ok(
                courseService.getCourses(search, teacherId, status, pageable)));
    }

    // GET /api/courses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.getCourseById(id)));
    }

    // POST /api/courses — ADMIN, TEACHER
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.ok("Tạo khóa học thành công",
                        courseService.createCourse(request)));
    }

    // PUT /api/courses/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCourseRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thành công",
                courseService.updateCourse(id, request)));
    }

    // PUT /api/courses/{id}/status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<CourseResponse>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCourseStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật status thành công",
                courseService.updateStatus(id, request.getStatus())));
    }

    // DELETE /api/courses/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa khóa học thành công", null));
    }
}