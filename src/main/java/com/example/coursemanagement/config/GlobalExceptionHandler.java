package com.example.coursemanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleApp(AppException ex) {
        log.warn("AppException: {}", ex.getMessage());
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex) {
        List<ApiResponse.FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ApiResponse.FieldError(e.getField(), e.getDefaultMessage()))
                .toList();
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.validationFail(errors));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccess(AccessDeniedException ex) {
        return ResponseEntity
                .status(403)
                .body(ApiResponse.fail("Không có quyền truy cập"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail("Lỗi hệ thống, vui lòng thử lại"));
    }
}