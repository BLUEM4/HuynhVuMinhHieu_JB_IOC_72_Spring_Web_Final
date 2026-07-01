package com.example.coursemanagement.dto.request;

import com.example.coursemanagement.entity.enums.CourseStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateCourseStatusRequest {

    @NotNull(message = "Status không được để trống")
    private CourseStatus status;
}