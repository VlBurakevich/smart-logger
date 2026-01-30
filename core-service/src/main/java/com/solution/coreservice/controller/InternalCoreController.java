package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.RegisterRequest;
import com.solution.coreservice.dto.request.StatusUpdateRequest;
import com.solution.coreservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal")
public class InternalCoreController {
    private final AccountService accountService;

    @GetMapping("/apiKey/exists")
    public ResponseEntity<Boolean> apiKeyExists(
            @RequestHeader("X-Api-Key-Hash") String apiKeyHash
    ){
        boolean exists = accountService.exists(apiKeyHash);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/account/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ){
        accountService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/account/{userId}/status")
    public ResponseEntity<Void> setIsActive(
            @PathVariable UUID userId,
            @Valid @RequestBody StatusUpdateRequest statusUpdateRequest
    ) {
        accountService.setIsActive(userId, statusUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/account/{userId}")
    public ResponseEntity<Void> deleteAccountById(
            @PathVariable UUID userId
    ) {
        accountService.deleteAccount(userId);
        return ResponseEntity.ok().build();
    }
}
