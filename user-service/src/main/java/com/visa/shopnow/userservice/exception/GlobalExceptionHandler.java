package com.visa.shopnow.userservice.exception;

import com.visa.shopnow.userservice.dto.UserApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<UserApiResponse<Void>> handleServiceException(UserServiceException ex) {
        HttpStatus status = ex.getErrorCode().getHttpStatus();
        UserApiResponse<Void> response = new UserApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UserApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        UserApiResponse<Map<String, String>> response = new UserApiResponse<>(false, errors, ErrorCode.VALIDATION_FAILED.getMessage());
        return new ResponseEntity<>(response, ErrorCode.VALIDATION_FAILED.getHttpStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserApiResponse<Void>> handleAllOtherExceptions(Exception ex) {
        UserApiResponse<Void> response = new UserApiResponse<>(false, null, ErrorCode.UNEXPECTED_ERROR.getMessage());
        return new ResponseEntity<>(response, ErrorCode.UNEXPECTED_ERROR.getHttpStatus());
    }
}
