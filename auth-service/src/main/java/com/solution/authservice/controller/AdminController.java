package com.solution.authservice.controller;

import com.solution.authservice.dto.request.StatusUpdateRequest;
import com.solution.authservice.dto.response.UserResponse;
import com.solution.authservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<Void> changeUserStatus(
            @PathVariable("userId") UUID userId,
            @RequestBody StatusUpdateRequest statusUpdateRequest
    ) {
        adminService.updateStatus(userId, statusUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("userId") UUID userId
    ) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
