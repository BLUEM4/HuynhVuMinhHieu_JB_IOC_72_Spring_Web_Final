package com.example.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class LessonResponse {
    private Integer lessonId;
    private Integer courseId;
    private String courseTitle;
    private String title;
    private String contentUrl;
    private String textContent;
    private Integer orderIndex;
    private boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}