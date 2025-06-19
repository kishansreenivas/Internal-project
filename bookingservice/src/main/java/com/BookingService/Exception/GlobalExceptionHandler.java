package com.BookingService.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.BookingService.payload.ApiResponse;


@RestControllerAdvice
public class GlobalExceptionHandler {
	  @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
	        Map<String, String> errors = new HashMap<>();
	        ex.getBindingResult().getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage()));
	        
	        return ResponseEntity.badRequest().body(ApiResponse.failure("Validation failed", errors));
	    }
}