package com.UserService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String success;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("true", data);
    }

    public static <T> ApiResponse<T> failure(T error) {
        return new ApiResponse<>("false", error);
    }
}
