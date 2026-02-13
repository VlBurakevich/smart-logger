package com.solution.authservice.controller;

import com.solution.authservice.dto.request.ChangePasswordRequest;
import com.solution.authservice.dto.response.UserResponse;
import com.solution.authservice.security.UserPrincipal;
import com.solution.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(userService.getUserInfo(userPrincipal.getId()));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        userService.changePassword(userPrincipal.getId(), request);
        return ResponseEntity.noContent().build();
    }
}
