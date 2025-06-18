package com.BookingService.Exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String,Object>> handleNF(
      ResourceNotFoundException ex, HttpServletRequest req) {
    Map<String,Object> b=new LinkedHashMap<>();
    b.put("timestamp",Instant.now());
    b.put("status",404);
    b.put("error","Not Found");
    b.put("message",ex.getMessage());
    b.put("path",req.getRequestURI());
    return ResponseEntity.status(404).body(b);
  }
}