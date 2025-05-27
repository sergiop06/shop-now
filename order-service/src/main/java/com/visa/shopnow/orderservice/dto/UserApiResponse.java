package com.visa.shopnow.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Lombok: Provides a builder pattern for object creation
public class UserApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    // You can also add convenience static methods for common responses
    public static <T> UserApiResponse<T> success(T data) {
        return UserApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("Operation successful")
                .build();
    }

    public static <T> UserApiResponse<T> success(T data, String message) {
        return UserApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> UserApiResponse<T> error(String message) {
        return UserApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .build();
    }

    public static <T> UserApiResponse<T> error(T data, String message) {
        return UserApiResponse.<T>builder()
                .success(false)
                .data(data)
                .message(message)
                .build();
    }
}
