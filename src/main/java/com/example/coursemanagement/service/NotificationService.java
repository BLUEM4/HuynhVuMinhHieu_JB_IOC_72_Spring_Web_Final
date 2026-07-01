package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.request.CreateNotificationRequest;
import com.example.coursemanagement.dto.response.NotificationResponse;
import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getMyNotifications(String username);
    NotificationResponse markAsRead(Integer notificationId, String username);
    NotificationResponse createNotification(CreateNotificationRequest request);
    void deleteNotification(Integer notificationId);
}