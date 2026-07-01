package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateStatusRequest {

    @NotNull(message = "Status không được để trống")
    private Boolean isActive;
}