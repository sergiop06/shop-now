package com.visa.shopnow.userservice.service;

import com.visa.shopnow.userservice.dto.AuthTokenResponseDTO;
import com.visa.shopnow.userservice.dto.LoginRequestDTO;
import com.visa.shopnow.userservice.dto.PasswordUpdateRequest;
import com.visa.shopnow.userservice.dto.UserRequest;
import com.visa.shopnow.userservice.dto.UserResponse;
import com.visa.shopnow.userservice.exception.ErrorCode;
import com.visa.shopnow.userservice.exception.UserServiceException;
import com.visa.shopnow.userservice.model.User;
import com.visa.shopnow.userservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponse registerUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new UserServiceException(ErrorCode.USER_ALREADY_EXISTS, "Username '" + userRequest.getUsername() + "' already exists.");
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserServiceException(ErrorCode.USER_ALREADY_EXISTS, "User with email '" + userRequest.getEmail() + "' already exists.");
        }

        User user = User.builder()
                .username(userRequest.getUsername())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .createdAt(LocalDateTime.now())
                .build();

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_CUSTOMER"); // Default role for new users
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return mapUserToUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + id));

        if (request.getUsername() != null && !request.getUsername().isBlank() && !existingUser.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UserServiceException(ErrorCode.USER_ALREADY_EXISTS, "Username '" + request.getUsername() + "' already exists.");
            }
            existingUser.setUsername(request.getUsername());
        }

        if (request.getName() != null && !request.getName().isBlank() && !existingUser.getName().equals(request.getName())) {
            existingUser.setName(request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank() && !existingUser.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserServiceException(ErrorCode.USER_ALREADY_EXISTS, "Email '" + request.getEmail() + "' already exists.");
            }
            existingUser.setEmail(request.getEmail());
        }

        existingUser.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(existingUser);
        return mapUserToUserResponse(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + id));
        return mapUserToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserServiceException(ErrorCode.USER_NOT_FOUND, "User not found with email: " + email));
        return mapUserToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserServiceException(ErrorCode.USER_NOT_FOUND, "User not found with username: " + username));
        return mapUserToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserServiceException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserServiceException(ErrorCode.USER_NOT_FOUND, "User not found with ID: " + userId));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UserServiceException(ErrorCode.PASSWORD_MISMATCH, "Incorrect current password.");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new UserServiceException(ErrorCode.PASSWORD_MISMATCH, "New password and confirm new password do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenResponseDTO loginUser(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(request.getUsernameOrEmail())
                        .orElseThrow(() -> new UserServiceException(ErrorCode.AUTHENTICATION_FAILED, "Invalid username or password.")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserServiceException(ErrorCode.AUTHENTICATION_FAILED, "Invalid username or password.");
        }

        String mockToken = "mock-jwt-token-for-" + user.getUsername() + "-" + System.currentTimeMillis();

        return AuthTokenResponseDTO.builder()
                .token(mockToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    private UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
