package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.ApiKeyCreateRequest;
import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/keys")
    public ResponseEntity<List<ApiKeyResponse>> getUserKeys(
        @RequestHeader("X-User-Id") UUID userId
    ) {
        return ResponseEntity.ok(accountService.getUserKeys(userId));
    }

    @PostMapping("/keys")
    public ResponseEntity<ApiKeyResponse> createNewKey(
        @RequestHeader("X-User-Id") UUID userId,
        @Valid @RequestBody ApiKeyCreateRequest keyRequest
    ) {
        return ResponseEntity.ok(accountService.createNewKey(keyRequest, userId));
    }

    @DeleteMapping("/keys/{keyId}")
    public ResponseEntity<Void> deleteKey(
        @RequestHeader("X-User-Id") UUID userId,
        @PathVariable UUID keyId
    ) {
        accountService.deleteKey(userId, keyId);
        return ResponseEntity.noContent().build();
    }
}


