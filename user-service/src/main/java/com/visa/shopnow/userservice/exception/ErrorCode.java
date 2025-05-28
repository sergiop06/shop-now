package com.visa.shopnow.userservice.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // User-related errors
    USER_NOT_FOUND("USER_001", "User not found.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER_002", "User with this username or email already exists.", HttpStatus.CONFLICT),

    // Authentication/Password-related errors
    PASSWORD_MISMATCH("AUTH_001", "Current password is incorrect or new passwords do not match.", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_FAILED("AUTH_002", "Invalid username or password.", HttpStatus.UNAUTHORIZED), // If you implement login logic

    // Generic/Fallback errors
    VALIDATION_FAILED("GEN_001", "Validation failed.", HttpStatus.BAD_REQUEST),
    UNEXPECTED_ERROR("GEN_002", "An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
