package com.visa.shopnow.productservice.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    PRODUCT_NOT_FOUND("PROD_001", "Product not found."),
    PRODUCT_NAME_ALREADY_EXISTS("PROD_002", "Product name already exists."),
    CATEGORY_NOT_FOUND("CAT_001", "Category not found."),
    CATEGORY_NAME_ALREADY_EXISTS("CAT_002", "Category name already exists."),
    INSUFFICIENT_STOCK("PROD_003", "Insufficient stock for product."),
    INVALID_INPUT("GEN_001", "Invalid input provided.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
