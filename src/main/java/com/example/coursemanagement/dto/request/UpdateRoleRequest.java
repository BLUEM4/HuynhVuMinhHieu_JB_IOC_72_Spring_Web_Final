package com.example.coursemanagement.dto.request;

import com.example.coursemanagement.entity.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateRoleRequest {

    @NotNull(message = "Role không được để trống")
    private UserRole role;
}