package com.novelgrain.common;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidation(MethodArgumentNotValidException e) {
        var msg = e.getBindingResult().getFieldErrors().stream().findFirst().map(f -> f.getField() + ": " + f.getDefaultMessage()).orElse("参数错误");
        return ApiResponse.err(40001, msg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIAE(IllegalArgumentException e) {
        return ApiResponse.err(40001, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleAny(Exception e) {
        return ApiResponse.err(50000, e.getMessage());
    }
}
