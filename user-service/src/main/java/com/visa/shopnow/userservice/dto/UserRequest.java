package com.visa.shopnow.userservice.dto;


import com.visa.shopnow.userservice.validation.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest { // Renamed from UserRegistrationRequestDTO

    @NotBlank(message = "Username cannot be empty", groups = OnCreate.class)
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Name cannot be empty", groups = OnCreate.class)
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty", groups = OnCreate.class)
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be empty", groups = OnCreate.class)
    @Size(min = 8, message = "Password must be at least 8 characters long", groups = OnCreate.class)
    private String password;
}
