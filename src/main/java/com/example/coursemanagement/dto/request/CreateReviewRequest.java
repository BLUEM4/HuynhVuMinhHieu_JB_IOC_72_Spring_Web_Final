package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateReviewRequest {

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating tối thiểu 1")
    @Max(value = 5, message = "Rating tối đa 5")
    private Integer rating;

    private String comment;
}