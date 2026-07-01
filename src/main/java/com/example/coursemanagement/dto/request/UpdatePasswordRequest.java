package com.example.coursemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdatePasswordRequest {

    @NotBlank(message = "Password cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Password mới không được để trống")
    @Size(min = 6, message = "Password mới phải tối thiểu 6 ký tự")
    private String newPassword;
}