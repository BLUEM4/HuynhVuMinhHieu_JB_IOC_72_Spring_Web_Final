package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(Integer userId);
    List<Notification> findByUser_UserIdAndIsRead(Integer userId, boolean isRead);
    long countByUser_UserIdAndIsRead(Integer userId, boolean isRead);
}