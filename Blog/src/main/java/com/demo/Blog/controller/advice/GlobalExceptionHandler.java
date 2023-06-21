package com.demo.Blog.controller.advice;

import com.demo.Blog.exception.response.ExceptionResponse;
import com.demo.Blog.exception.response.ExceptionValidatorResponse;
import com.demo.Blog.exception.user.UserEmailAlreadyInUseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionValidatorResponse> handle(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity
                .ok(new ExceptionValidatorResponse(
                        "Validation Failed",
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now().toString(),
                        request.getServletPath(),
                        errors));
    }
    @ExceptionHandler(UserEmailAlreadyInUseException.class)
    public ResponseEntity<ExceptionResponse> handle(UserEmailAlreadyInUseException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now().toString(),
                        request.getServletPath()));
    }




}
