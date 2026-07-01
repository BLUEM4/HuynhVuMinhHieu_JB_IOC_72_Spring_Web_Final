package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CreateLessonRequest;
import com.example.coursemanagement.dto.request.UpdateLessonRequest;
import com.example.coursemanagement.dto.response.LessonResponse;
import java.util.List;

public interface LessonService {
    List<LessonResponse> getLessonsByCourse(Integer courseId, String username);
    LessonResponse getLessonById(Integer lessonId, String username);
    LessonResponse createLesson(Integer courseId, CreateLessonRequest request, String username);
    LessonResponse updateLesson(Integer lessonId, UpdateLessonRequest request, String username);
    LessonResponse publishLesson(Integer lessonId, String username);
    void deleteLesson(Integer lessonId, String username);
}