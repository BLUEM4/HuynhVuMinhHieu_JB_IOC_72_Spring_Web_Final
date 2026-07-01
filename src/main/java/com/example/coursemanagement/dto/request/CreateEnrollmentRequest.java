package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateEnrollmentRequest {

    @NotNull(message = "Course ID không được để trống")
    private Integer courseId;
}