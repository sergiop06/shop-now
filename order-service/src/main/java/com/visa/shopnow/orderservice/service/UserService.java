package com.visa.shopnow.orderservice.service;

import com.visa.shopnow.orderservice.dto.PasswordUpdateRequest;
import com.visa.shopnow.orderservice.dto.UserRequest;
import com.visa.shopnow.orderservice.dto.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse registerUser(UserRequest request);
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    UserResponse getUserByUsername(String username);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserRequest request); // Changed to UserRequest
    void deleteUser(Long id);
    void updatePassword(Long userId, PasswordUpdateRequest request); //separated because of security reasons
}

