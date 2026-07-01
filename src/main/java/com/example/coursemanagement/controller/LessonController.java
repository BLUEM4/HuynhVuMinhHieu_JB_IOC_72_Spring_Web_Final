package com.example.coursemanagement.controller;

import com.example.coursemanagement.config.ApiResponse;
import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.request.CreateLessonRequest;
import com.example.coursemanagement.dto.request.UpdateLessonRequest;
import com.example.coursemanagement.dto.response.LessonResponse;
import com.example.coursemanagement.entity.Lesson;
import com.example.coursemanagement.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    // GET /api/courses/{courseId}/lessons
    @GetMapping("/api/courses/{courseId}/lessons")
    public ResponseEntity<ApiResponse<List<LessonResponse>>> getLessonsByCourse(
            @PathVariable Integer courseId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                lessonService.getLessonsByCourse(courseId, userDetails.getUsername())));
    }

    // POST /api/courses/{courseId}/lessons
    @PostMapping("/api/courses/{courseId}/lessons")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(
            @PathVariable Integer courseId,
            @Valid @RequestBody CreateLessonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(201).body(ApiResponse.ok("Tạo lesson thành công",
                lessonService.createLesson(courseId, request, userDetails.getUsername())));
    }

    // GET /api/lessons/{id}
    @GetMapping("/api/lessons/{id}")
    public ResponseEntity<ApiResponse<LessonResponse>> getLessonById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok(
                lessonService.getLessonById(id, userDetails.getUsername())));
    }

    // GET /api/lessons/{id}/content_preview
    @GetMapping("/api/lessons/{id}/content_preview")
    public ResponseEntity<ApiResponse<Map<String, String>>> getContentPreview(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {

        LessonResponse lesson = lessonService.getLessonById(id, userDetails.getUsername());

        String preview = lesson.getTextContent() != null
                ? lesson.getTextContent().substring(0, Math.min(200, lesson.getTextContent().length()))
                : "";

        return ResponseEntity.ok(ApiResponse.ok(Map.of(
                "lessonId", String.valueOf(lesson.getLessonId()),
                "title", lesson.getTitle(),
                "contentPreview", preview,
                "contentUrl", lesson.getContentUrl() != null ? lesson.getContentUrl() : ""
        )));
    }

    // PUT /api/lessons/{id}
    @PutMapping("/api/lessons/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateLessonRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật thành công",
                lessonService.updateLesson(id, request, userDetails.getUsername())));
    }

    // PUT /api/lessons/{id}/publish
    @PutMapping("/api/lessons/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<LessonResponse>> publishLesson(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.ok("Publish thành công",
                lessonService.publishLesson(id, userDetails.getUsername())));
    }

    // DELETE /api/lessons/{id}
    @DeleteMapping("/api/lessons/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        lessonService.deleteLesson(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.ok("Xóa lesson thành công", null));
    }
}