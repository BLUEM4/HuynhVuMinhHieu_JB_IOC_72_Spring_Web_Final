package com.example.coursemanagement.mapper;

import com.example.coursemanagement.dto.response.LessonResponse;
import com.example.coursemanagement.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
                .lessonId(lesson.getLessonId())
                .courseId(lesson.getCourse().getCourseId())
                .courseTitle(lesson.getCourse().getTitle())
                .title(lesson.getTitle())
                .contentUrl(lesson.getContentUrl())
                .textContent(lesson.getTextContent())
                .orderIndex(lesson.getOrderIndex())
                .isPublished(lesson.isPublished())
                .createdAt(lesson.getCreatedAt())
                .updatedAt(lesson.getUpdatedAt())
                .build();
    }
}