package com.solution.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String accessToken;
    private String refreshToken;

    private UUID userId;
    private String username;
    private String email;
    private Boolean isActive;
    private List<String> roles;
}
