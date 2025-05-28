package com.visa.shopnow.userservice.exception;

import com.visa.shopnow.userservice.dto.UserApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // HTTP status to 404
    public ResponseEntity<UserApiResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        UserApiResponse<Void> response = new UserApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ResponseEntity<UserApiResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        UserApiResponse<Void> response = new UserApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP status code to 400
    public ResponseEntity<UserApiResponse<Void>> handlePasswordMismatchException(PasswordMismatchException ex) {
        UserApiResponse<Void> response = new UserApiResponse<>(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP status code to 400
    public ResponseEntity<UserApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        UserApiResponse<Map<String, String>> response = new UserApiResponse<>(false, errors, "Validation failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
