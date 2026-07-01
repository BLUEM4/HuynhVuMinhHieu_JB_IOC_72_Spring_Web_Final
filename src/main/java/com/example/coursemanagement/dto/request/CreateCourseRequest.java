package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class CreateCourseRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    private String title;

    private String description;

    @NotNull(message = "Teacher ID không được để trống")
    private Integer teacherId;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá không được âm")
    private BigDecimal price;

    @Min(value = 1, message = "Số giờ phải lớn hơn 0")
    private Integer durationHours;
}