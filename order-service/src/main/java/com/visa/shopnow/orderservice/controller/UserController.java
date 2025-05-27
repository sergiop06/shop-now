 package com.visa.shopnow.orderservice.controller;

 import com.visa.shopnow.orderservice.dto.UserApiResponse;
 import com.visa.shopnow.orderservice.dto.UserRequest;
 import com.visa.shopnow.orderservice.dto.UserResponse;
 import com.visa.shopnow.orderservice.service.UserService;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.GetMapping;
 import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.PostMapping;
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
     public ResponseEntity<UserApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
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

 }
