package com.example.coursemanagement.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final List<FieldError> errors;
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Getter
    public static class FieldError {
        private final String field;
        private final String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }

    private ApiResponse(boolean success, String message, T data, List<FieldError> errors) {
        this.success = success;
        this.message = message;
        this.data    = data;
        this.errors  = errors;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Thao tác thành công", data, null);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null, null);
    }

    public static <T> ApiResponse<T> validationFail(List<FieldError> errors) {
        return new ApiResponse<>(false, "Dữ liệu không hợp lệ", null, errors);
    }
}