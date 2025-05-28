package com.visa.shopnow.productservice.exception;

import com.visa.shopnow.productservice.dto.ProductApiResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ProductApiResponse<Void>> handleProductServiceException(ProductServiceException ex) {
        return new ResponseEntity<>(
                ProductApiResponse.error(ex.getMessage(), ex.getErrorCode().getCode()),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProductApiResponse<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                ProductApiResponse.error(errors.toString(), ErrorCode.INVALID_INPUT.getCode()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProductApiResponse<Void>> handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ProductApiResponse.error("An unexpected error occurred: " + ex.getMessage(), "SERVER_ERROR"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

