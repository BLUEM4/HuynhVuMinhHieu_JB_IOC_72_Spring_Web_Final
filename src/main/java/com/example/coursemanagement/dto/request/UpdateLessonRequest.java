package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateLessonRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 255, message = "Tiêu đề tối đa 255 ký tự")
    private String title;

    private String contentUrl;

    private String textContent;

    @NotNull(message = "Thứ tự không được để trống")
    @Min(value = 1, message = "Thứ tự phải lớn hơn 0")
    private Integer orderIndex;
}