package com.solution.authservice.controller;

import com.solution.authservice.dto.request.LogoutRequest;
import com.solution.authservice.dto.response.AuthResponse;
import com.solution.authservice.dto.request.RefreshRequest;
import com.solution.authservice.dto.response.RegisterResponse;
import com.solution.authservice.dto.request.UserLoginRequest;
import com.solution.authservice.dto.request.UserRegisterRequest;
import com.solution.authservice.security.UserPrincipal;
import com.solution.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody UserRegisterRequest userRegisterRequest
    ) {
        return ResponseEntity.ok(authService.register(userRegisterRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody UserLoginRequest userLoginRequest
    ) {
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest logoutRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        authService.logout(logoutRequest, userPrincipal);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody RefreshRequest refreshRequest
    ) {
        return ResponseEntity.ok(authService.refresh(refreshRequest));
    }
}
