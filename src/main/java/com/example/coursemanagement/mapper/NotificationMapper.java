package com.example.coursemanagement.mapper;

import com.example.coursemanagement.dto.response.NotificationResponse;
import com.example.coursemanagement.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .userId(n.getUser().getUserId())
                .username(n.getUser().getUsername())
                .message(n.getMessage())
                .type(n.getType())
                .targetUrl(n.getTargetUrl())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}