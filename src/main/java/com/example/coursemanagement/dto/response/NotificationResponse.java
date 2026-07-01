package com.example.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Integer notificationId;
    private Integer userId;
    private String username;
    private String message;
    private String type;
    private String targetUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
}