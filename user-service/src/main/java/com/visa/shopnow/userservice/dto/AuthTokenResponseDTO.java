package com.visa.shopnow.userservice.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenResponseDTO {
    private String token;
    private String username;
    private String email;
    private Set<String> roles;
}
