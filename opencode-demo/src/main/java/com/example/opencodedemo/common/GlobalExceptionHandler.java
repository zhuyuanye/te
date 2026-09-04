package com.example.opencodedemo.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/** 将常见业务和校验异常转换成稳定的 API 错误结构。 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(
                new ApiError("INVALID_ARGUMENT", exception.getMessage(), Instant.now())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage() == null
                        ? "请求参数不合法"
                        : error.getDefaultMessage())
                .orElse("请求参数不合法");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiError("VALIDATION_FAILED", message, Instant.now())
        );
    }
}
