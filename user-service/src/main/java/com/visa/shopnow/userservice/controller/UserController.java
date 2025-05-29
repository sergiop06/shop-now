 package com.visa.shopnow.userservice.controller;

 import com.visa.shopnow.userservice.dto.AuthTokenResponseDTO;
 import com.visa.shopnow.userservice.dto.LoginRequestDTO;
 import com.visa.shopnow.userservice.dto.PasswordUpdateRequest;
 import com.visa.shopnow.userservice.dto.UserApiResponse;
 import com.visa.shopnow.userservice.dto.UserRequest;
 import com.visa.shopnow.userservice.dto.UserResponse;
 import com.visa.shopnow.userservice.service.UserService;
 import com.visa.shopnow.userservice.validation.OnCreate;
 import jakarta.validation.Valid;
 import java.util.List;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.validation.annotation.Validated;
 import org.springframework.web.bind.annotation.DeleteMapping;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PatchMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.PutMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;

 @RestController
 @RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

     public UserController(UserService userService) {
         this.userService = userService;
     }

     // POST /api/v1/users
     @PostMapping
     public ResponseEntity<UserApiResponse<UserResponse>> createUser(
             @Validated(OnCreate.class) @RequestBody UserRequest request) {
         UserResponse userResponse = userService.registerUser(request);
         UserApiResponse<UserResponse> response = new UserApiResponse<>(true, userResponse, "User created successfully");
         return new ResponseEntity<>(response, HttpStatus.CREATED);
     }

     // GET /api/v1/users/{id}
     @GetMapping("/{id}")
     public ResponseEntity<UserApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
         UserResponse userResponse = userService.getUserById(id);
         UserApiResponse<UserResponse> response = new UserApiResponse<>(true, userResponse, null);
         return ResponseEntity.ok(response);
     }

     // GET /api/v1/users
     @GetMapping()
     public ResponseEntity<UserApiResponse<List<UserResponse>>> getAllUsers() {
         List<UserResponse> allUsers = userService.getAllUsers();
         UserApiResponse<List<UserResponse>> response = new UserApiResponse<>(true, allUsers, null);
         return ResponseEntity.ok(response);
     }

     // PUT /api/v1/users/{id}
     @PutMapping("/{id}")
     public ResponseEntity<UserApiResponse<UserResponse>> updateUser(
             @PathVariable Long id,
             @Valid @RequestBody UserRequest request) {
         UserResponse updatedUser = userService.updateUser(id, request);
         return ResponseEntity.ok(UserApiResponse.success(updatedUser, "User updated successfully."));
     }

     // PATCH /api/v1/users/{id}/password
     @PatchMapping("/{id}/password")
     public ResponseEntity<UserApiResponse<UserResponse>> updatePassword(
             @PathVariable Long id,
             @Valid @RequestBody PasswordUpdateRequest request) {
         userService.updatePassword(id, request);
         return ResponseEntity.ok(UserApiResponse.success(null, "Password updated successfully."));
     }

     // DELETE /api/v1/users/{id}
     @DeleteMapping("/{id}")
     public ResponseEntity<UserApiResponse<Void>> deleteUser(@PathVariable Long id) {
         userService.deleteUser(id);
         return ResponseEntity.ok(UserApiResponse.success(null, "User deleted successfully."));
     }

     // POST /api/v1/users/login
     @PostMapping("/login")
     public ResponseEntity<UserApiResponse<AuthTokenResponseDTO>> loginUser(
             @Valid @RequestBody LoginRequestDTO request) {
         AuthTokenResponseDTO tokenResponse = userService.loginUser(request);
         return ResponseEntity.ok(UserApiResponse.success(tokenResponse, "User logged in successfully."));
     }

 }
