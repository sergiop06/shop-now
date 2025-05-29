package com.visa.shopnow.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String errorCode;

    public static <T> ProductApiResponse<T> success(T data, String message) {
        return new ProductApiResponse<>(true, data, message, null);
    }

    public static <T> ProductApiResponse<T> success(String message) {
        return new ProductApiResponse<>(true, null, message, null);
    }

    public static <T> ProductApiResponse<T> error(String message, String errorCode) {
        return new ProductApiResponse<>(false, null, message, errorCode);
    }

    public static <T> ProductApiResponse<T> error(String message) {
        return new ProductApiResponse<>(false, null, message, null);
    }
}
