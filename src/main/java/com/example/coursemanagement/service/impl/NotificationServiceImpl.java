package com.example.coursemanagement.service.impl;

import com.example.coursemanagement.config.AppException;
import com.example.coursemanagement.dto.request.CreateNotificationRequest;
import com.example.coursemanagement.dto.response.NotificationResponse;
import com.example.coursemanagement.entity.Notification;
import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.mapper.NotificationMapper;
import com.example.coursemanagement.repository.NotificationRepository;
import com.example.coursemanagement.repository.UserRepository;
import com.example.coursemanagement.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(String username) {
        User user = findUserByUsername(username);
        return notificationRepository
                .findByUser_UserIdOrderByCreatedAtDesc(user.getUserId())
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Integer notificationId, String username) {
        Notification notification = findById(notificationId);
        User user = findUserByUsername(username);

        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw AppException.forbidden("Bạn không có quyền cập nhật thông báo này");
        }

        notification.setIsRead(true);
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));

        Notification notification = Notification.builder()
                .user(user)
                .message(request.getMessage())
                .type(request.getType())
                .targetUrl(request.getTargetUrl())
                .isRead(false)
                .build();

        return notificationMapper.toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void deleteNotification(Integer notificationId) {
        if (!notificationRepository.existsById(notificationId))
            throw AppException.notFound("Thông báo không tồn tại");
        notificationRepository.deleteById(notificationId);
    }

    private Notification findById(Integer id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> AppException.notFound("Thông báo không tồn tại"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> AppException.notFound("User không tồn tại"));
    }
}