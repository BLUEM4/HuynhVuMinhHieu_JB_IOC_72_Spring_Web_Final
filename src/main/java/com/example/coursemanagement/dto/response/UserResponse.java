package com.example.coursemanagement.dto.response;

import com.example.coursemanagement.entity.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}